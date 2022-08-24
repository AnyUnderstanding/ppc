package data

class DocumentInformation(val name: String, val type: DocumentInformationType, val path: String) {
    val children = mutableListOf<DocumentInformation>()

}

enum class DocumentInformationType{
    Document, Folder
}