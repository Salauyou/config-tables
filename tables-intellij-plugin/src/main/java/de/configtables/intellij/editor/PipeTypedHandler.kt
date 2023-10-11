package de.configtables.intellij.editor

import com.intellij.codeInsight.editorActions.TypedHandlerDelegate
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.EditorModificationUtil
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.codeStyle.CodeStyleManager
import de.configtables.intellij.TablesFileType

class PipeTypedHandler : TypedHandlerDelegate() {

    override fun charTyped(c: Char, project: Project, editor: Editor, file: PsiFile): Result {
        if (file.fileType == TablesFileType.INSTANCE && c == '|') {
            // TODO: check we are not in quoted text
            val offset = editor.caretModel.offset
            EditorModificationUtil.insertStringAtCaret(editor, " ")
            CodeStyleManager.getInstance(project).reformatText(file, offset - 1, offset)
        }
        return super.charTyped(c, project, editor, file)
    }
}