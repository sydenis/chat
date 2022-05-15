const val UNEXISTING_MESSAGE = -1

data class Chat(
    val id: UInt,
    val owner: UInt,
    val recipient: UInt,
    var unreadCount: UInt = 0U
)

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