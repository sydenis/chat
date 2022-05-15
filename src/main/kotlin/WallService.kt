import java.util.concurrent.atomic.AtomicInteger

const val ERROR_NOT_FOUND = 180

object WallService {
    private var chats = mutableListOf<Chat>()
    private var chatIdGen: AtomicInteger = AtomicInteger()

    private var messages = mutableListOf<Message>()
    private var messageIdGen: AtomicInteger = AtomicInteger()

    fun chatAdd(owner: UInt, recipient: UInt): UInt {
        if (owner == recipient)
           throw RuntimeException("Собеседники чата не могут быть одним человеком")

        if (
            chats.filter {
                it.owner == owner &&
                        it.recipient == recipient
            }.isNotEmpty()
            ||
            chats.filter {
                it.owner == recipient &&
                        it.recipient == owner
            }.isNotEmpty()
        )
            throw RuntimeException("Чат уже существует")


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

    fun chatsByUser(user: UInt): List<Chat> {
        return chats.filter {
            user in setOf(it.owner, it.recipient)
        }
    }

    fun chatUnreadCount(user: UInt): Int {
        return chatsByUser(user).filter {
            it.unreadCount != 0U
        }.count()
    }

    fun chatGetUnreadMessages(chatId: UInt, recipient: UInt, offset: Int, count: Int): List<Message>{
        val chat = chatById(chatId) ?: throw RuntimeException("Чат не существует")

        var newMessages =  messages
            .filter {
                it.chatId == chatId &&
                it.fromUser != recipient &&
                !it.isRead
            }
            .sortedBy { it.id }

        val actualCnt = if (count == 0)
            newMessages.count() - offset
        else
            count

        newMessages = newMessages.subList(offset, offset + actualCnt)

        newMessages.forEach { it.isRead = true }

        val unreadChatCnt = chat.unreadCount
        val unreadMsgCnt = newMessages.count()

        if (unreadMsgCnt.toUInt() <= unreadChatCnt)
            chat.unreadCount -= unreadMsgCnt.toUInt()
        else
            chat.unreadCount = 0U

        return newMessages
    }

    fun messageAdd(chatId: UInt, fromUser: UInt, text: String): UInt {
        val chat = chatById(chatId) ?: throw RuntimeException("Чат не существует")

        val id = messageIdGen.getAndIncrement().toUInt()
        messages.add(Message(id, chatId, fromUser, text))

        chat.unreadCount++

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

    fun clear() {
        chats = mutableListOf<Chat>()
        chatIdGen.set(0)

        messages = mutableListOf<Message>()
        messageIdGen.set(0)
    }

}
