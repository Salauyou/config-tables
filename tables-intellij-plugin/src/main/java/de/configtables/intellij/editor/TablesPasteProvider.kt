package de.configtables.intellij.editor

import com.intellij.ide.PasteProvider
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.EditorModificationUtil
import com.intellij.openapi.editor.actions.PasteAction
import com.intellij.openapi.ide.CopyPasteManager
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.codeStyle.CodeStyleManager
import com.intellij.refactoring.suggested.endOffset
import de.configtables.intellij.psi.elements.TableElements.Companion.isPipe
import groovy.json.StringEscapeUtils
import java.awt.datatransfer.DataFlavor
import java.io.Reader

class TablesPasteProvider : PasteProvider {

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }

    override fun performPaste(dataContext: DataContext) {
        val project = CommonDataKeys.PROJECT.getData(dataContext) ?: return
        val file = CommonDataKeys.PSI_FILE.getData(dataContext) ?: return
        val editor = CommonDataKeys.EDITOR.getData(dataContext) ?: return
        val clip = PasteAction.TRANSFERABLE_PROVIDER.getData(dataContext)?.produce() ?: return
        val lines = (clip.getTransferData(DataFlavor.plainTextFlavor) as Reader).readLines()
        var reformat = false
        var startWithPipe = true
        var endWithPipe = true
        // find previous non-whitespace left to caret
        // to decide should we put '|' at the start
        var elementOnCaret = file.findElementAt(Math.max(0, editor.caretModel.offset - 1))
        if (elementOnCaret is PsiWhiteSpace) {
            elementOnCaret = file.findElementAt(Math.max(0, elementOnCaret.textOffset - 1))
        }
        if (elementOnCaret.isPipe()) {
            startWithPipe = false
        }
        // find next non-whitespace right to caret
        // to decide should we need '|' at the end
        elementOnCaret = file.findElementAt(editor.caretModel.offset)
        val increaseReformatOffset = elementOnCaret != null  // not EOF
        if (elementOnCaret is PsiWhiteSpace) {
            elementOnCaret = file.findElementAt(elementOnCaret.endOffset)
        }
        if (elementOnCaret?.text == "|") {
            endWithPipe = false
        }
        val pasteText = StringBuilder()
        lines.mapIndexed { index, line ->
            if (line.contains('\t')) {
                reformat = true
                if (startWithPipe || index > 0) {
                    pasteText.append('|')
                }
                line.splitToSequence('\t').map {
                    when {
                        it.isNotEmpty() && it[0].let { it == '\'' || it == '\"' } -> it.quoted()
                        else -> it
                    }
                }.forEach {
                    pasteText.append(it).append('|')
                }
                if (index == lines.size - 1 && !endWithPipe) {
                    // remove ending '|'
                    pasteText.deleteCharAt(pasteText.length - 1)
                }
            } else {
                pasteText.append(line)
            }
            if (index < lines.size - 1) {
                pasteText.append('\n')
            }
        }
        WriteCommandAction.writeCommandAction(project).withName("Paste from Clipboard").run<Exception> {
            val startOffset = editor.caretModel.offset
            EditorModificationUtil.insertStringAtCaret(editor, pasteText.toString())
            if (reformat) {
                var endOffset = startOffset + pasteText.length
                if (increaseReformatOffset) {
                    ++endOffset
                }
                CodeStyleManager.getInstance(project).reformatText(file, startOffset, endOffset)
            }
        }
    }

    override fun isPastePossible(dataContext: DataContext) = true

    override fun isPasteEnabled(dataContext: DataContext): Boolean {
       return CopyPasteManager.getInstance().getContents<String>(DataFlavor.plainTextFlavor) != null
    }

    private fun String.quoted() = '\"' + StringEscapeUtils.escapeJava(this) + '\"'
}