import java.util.concurrent.atomic.AtomicInteger

const val ERROR_NOT_FOUND = 180

object WallService {
    private var chats = mutableListOf<Chat>()
    private var chatIdGen: AtomicInteger = AtomicInteger()

    private var messages = mutableListOf<Message>()
    private var messageIdGen: AtomicInteger = AtomicInteger()

    fun chatAdd(owner: UInt, recipient: UInt): UInt {
        val id = chatIdGen.getAndIncrement().toUInt()
        chats.add(Chat(id, owner, recipient))
        return id
    }

    private fun chatById(id: UInt): Chat? {
        return chats.filter { it.id == id }.first()
    }

    fun chatDel(id: UInt): Boolean {
        messages.removeAll { it.chatId == id }
        return chats.remove(chatById(id))
    }

    fun chatsByOwner(owner: UInt): List<Chat> {
        return chats.filter { it.owner == owner }
    }

    fun chatUnreadCount(owner: UInt): Int {
        return chatsByOwner(owner).filter {
            it.lastUnreadMsg != UNEXISTING_MESSAGE
        }.count()
    }

    fun chatGetUnreadMessages(chatId: UInt, recipient: UInt, offset: Int, count: Int): List<Message>{
        var newMessages =  messages
            .filter {
                it.chatId == chatId &&
                it.fromUser != recipient
            }
            .sortedBy { it.id }
            .subList(offset, offset + count)

        newMessages.forEach { it.isRead = true }

        return newMessages
    }

    fun messageAdd(chatId: UInt, fromUser: UInt, text: String): UInt {
        val id = messageIdGen.getAndIncrement().toUInt()
        messages.add(Message(id, chatId, fromUser, text))
        return id
    }

    private fun messageById(id: UInt): Message? {
        return messages.filter { it.id == id }.first()
    }

    fun messageDel(id: UInt): Boolean {
        val message = messageById(id) ?: return false

        val chatId = message.chatId

        val result = messages.remove(message)

        if (messages.filter { it.id == id }.isEmpty())
            chatDel(chatId)

        return result
    }

}
