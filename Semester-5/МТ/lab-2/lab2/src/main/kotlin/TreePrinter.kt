import java.io.Writer

class TreePrinter(private val writer: Writer) {
    fun print(tree: Tree) {
        writer.write("digraph mygraph {\n")
        writer.write(PREAMBLE)
        dfs(tree)
        writer.write("}\n")
        writer.flush()
    }

    private fun dfs(tree: Tree) {
        writer.write("\"${tree}\"\n")
        writer.write("\"${tree}\" [label=\"${tree.node}\"]\n")
        tree.children.forEach {
            writer.write("\"${tree}\" -> \"${it}\"\n")
        }
        tree.children.forEach { dfs(it) }
    }

    companion object {
        private const val PREAMBLE = "  fontname=\"Helvetica,Arial,sans-serif\"\n" +
            "  node [fontname=\"Helvetica,Arial,sans-serif\"]\n" +
            "  edge [fontname=\"Helvetica,Arial,sans-serif\"]\n" +
            "  node [shape=box];\n"
    }
}
