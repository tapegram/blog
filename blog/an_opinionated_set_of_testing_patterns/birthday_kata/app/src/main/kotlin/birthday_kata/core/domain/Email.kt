package birthday_kata.core.domain

typealias Emails = List<Email>

data class Email(
    val subject: EmailSubject,
    val to: EmailAddress,
    val body: MessageBody,
)

typealias EmailSubject = String
typealias MessageBody = String

data class EmailAddress(
    val localPart: LocalPart,
    val domainName: DomainName,
) {
    override fun toString(): String =
        "$localPart@$domainName"
}

typealias LocalPart = String

data class DomainName(
    val name: String,
    val extension: Extension
) {
    override fun toString(): String =
        "$name.$extension"
}

enum class Extension {
    COM,
    EDU,
    NET,
    ORG;

    override fun toString(): String =
        when (this) {
            COM -> "com"
            EDU -> "edu"
            NET -> "net"
            ORG -> "org"
        }
}
