import org.apache.maven.plugin.AbstractMojo
import org.apache.maven.plugins.annotations.Mojo
import org.apache.maven.plugins.annotations.Parameter
import org.apache.maven.project.MavenProject
import org.codehaus.plexus.util.DirectoryScanner
import java.nio.file.Paths

@Mojo(name = "generate-sql")
class ConfigTablesGenerateSqlMojo : AbstractMojo() {

    @Parameter(property = "generate-sql.sourceFolder", defaultValue = "src/main/resources")
    private lateinit var sourceFolder: String

    @Parameter(property = "generate-sql.targetFolder", defaultValue = "target/generated-resources")
    private lateinit var targetFolder: String

    @Parameter(property = "generate-sql.fileFilter", defaultValue = "**/*.ct")
    private lateinit var fileFilter: String

    @Parameter(defaultValue = "\${project}", readonly = true)
    private lateinit var project: MavenProject

    override fun execute() {
        val basePath = project.basedir.path
        val sourcePath = Paths.get(basePath, sourceFolder)
        val targetPath = Paths.get(basePath, targetFolder)
        log.info("Source folder=${sourcePath.toAbsolutePath()}")
        log.info("Target folder=${targetPath.toAbsolutePath()}")

        log.info("Reading source files")
        val directoryScanner = DirectoryScanner().also {
            it.setBasedir(sourcePath.toAbsolutePath().toString())
            it.setCaseSensitive(false)
            it.setIncludes(fileFilter.split(',').toTypedArray())
        }
        directoryScanner.scan()
        directoryScanner.includedFiles.forEach {
            log.info(sourcePath.resolve(it).toAbsolutePath().toString())
        }
    }
}