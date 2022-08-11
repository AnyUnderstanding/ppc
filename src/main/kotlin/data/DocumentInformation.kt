package data

class DocumentInformation(val name: String, val type: DocumentInformationType, val path: String) {
    val children = mutableListOf<DocumentInformation>()

    fun addChild(documentInformation: DocumentInformation){
        if (type!=DocumentInformationType.Folder) throw IllegalArgumentException("Children can only be added to DocumentInformation when type is equals to DocumentInformationType.Folder")
    }
}

enum class DocumentInformationType{
    Document, Folder
}