const val UNEXISTING_MESSAGE = -1

data class Chat(
    val id: UInt,
    val owner: UInt,
    val recipient: UInt,
    var lastUnreadMsg: Int = UNEXISTING_MESSAGE
) {
    fun lastMessage(): String {
        return if (lastUnreadMsg == UNEXISTING_MESSAGE)
            "Нет сообщений"
        else
            ""
    }
}

data class Message(
    val id: UInt,
    val chatId: UInt,
    val fromUser: UInt,
    var text: String,
    var isRead : Boolean = false
)

data class User(
    val id: UInt,
    val name: String
)