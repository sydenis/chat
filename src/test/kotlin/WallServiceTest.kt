import org.junit.Before
import org.junit.Test

import org.junit.Assert.*

class WallServiceTest {

    @Before
    fun setUp() {
        WallService.clear()
        val user1 = 1U
        val user2 = 2U
        val idChat = WallService.chatAdd(user1, user2)
        WallService.messageAdd(idChat, user1, "Hello")
    }

    @Test
    fun chatAdd() {
        val expected = 1U
        val actual = WallService.chatAdd(3U, 4U)

        assertEquals(expected, actual)
    }

    @Test (expected = RuntimeException::class)
    fun chatAdd_existing() {
        WallService.chatAdd(2U, 1U)
    }

    @Test (expected = RuntimeException::class)
    fun chatAdd_same_user() {
        WallService.chatAdd(5U, 5U)
    }

    @Test
    fun chatDel() {
        val firstChat = 0U
        val expected = true
        val actual = WallService.chatDel(firstChat)

        assertEquals(expected, actual)
    }

    @Test
    fun chatsByUser() {
        val user1 = 1U
        val expected = 1
        val actual = WallService.chatsByUser(user1).count()

        assertEquals(expected, actual)
    }

    @Test
    fun chatUnreadCount() {
        val user1 = 1U
        val expected = 1
        val actual = WallService.chatUnreadCount(user1)

        assertEquals(expected, actual)
    }

    @Test (expected = RuntimeException::class)
    fun chatGetUnreadMessages_no_chat() {
        val user1 = 1U
        WallService.chatGetUnreadMessages(555U, user1, 0, 100)
    }

    @Test
    fun chatGetUnreadMessages() {
        val firstChat = 0U
        val user2 = 2U
        val expected = 1

        val actual = WallService.chatGetUnreadMessages(firstChat, user2,0, 0).count()

        assertEquals(expected, actual)
    }

    @Test
    fun messageAdd() {
        val firstChat = 0U
        val user1 = 1U
        val expected = 1U
        val actual = WallService.messageAdd(firstChat, user1, "test1")

        assertEquals(expected, actual)
    }

    @Test
    fun messageDel() {
        val firstMsg = 0U
        val expected = true
        val actual = WallService.messageDel(firstMsg)

        assertEquals(expected, actual)
    }
}