package cat.xlagunas.andrtc.user

import cat.xlagunas.andrtc.user.UserNotFoundException
import cat.xlagunas.andrtc.user.UserConverter
import cat.xlagunas.andrtc.user.UserDto
import cat.xlagunas.andrtc.user.UserRepository
import cat.xlagunas.andrtc.user.UserService
import cat.xlagunas.andrtc.user.UserServiceImpl

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.MockitoAnnotations

import org.assertj.core.api.Assertions.assertThat
import org.mockito.ArgumentMatchers
import org.mockito.Matchers.anyLong
import org.mockito.Mockito.`when`

@RunWith(JUnit4::class)
class UserServiceImplTest {
    @Mock
    private lateinit var userRepository: UserRepository
    private lateinit var userService: UserService

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        userService = UserServiceImpl(userRepository)
    }

    @Test
    @Throws(UserNotFoundException::class)
    fun givenExistingUser_whenFindUser_thenReturnUser() {
        val userDto = UserDto.Builder()
                .username("aUsername")
                .password("aPassword")
                .firstname("aFirstName")
                .lastname("aSecondName")
                .email("anEmail@direction.com")
                .build()
        `when`<User>(userRepository.findUser(ArgumentMatchers.anyLong())).thenReturn(UserConverter.convert(userDto))

        val responseDto = userService.findUser(10)

        assertThat(responseDto).isEqualToComparingFieldByField(userDto)
    }

    @Test(expected = UserNotFoundException::class)
    @Throws(UserNotFoundException::class)
    fun givenNonExistingUser_whenFindUser_thenReturnUser() {
        val userDto = UserDto.Builder()
                .username("aUsername")
                .password("aPassword")
                .build()
        `when`<User>(userRepository.findUser(ArgumentMatchers.anyLong()))
                .thenThrow(UserNotFoundException("User not found on db", RuntimeException()))

        val responseDto = userService.findUser(10)

        assertThat(responseDto).isEqualToComparingFieldByField(userDto)
    }

    @Test
    @Throws(Exception::class)
    fun searchByUsername() {
    }

    @Test
    @Throws(Exception::class)
    fun createUser() {
    }

    @Test
    @Throws(Exception::class)
    fun updatePassword() {
    }

    @Test
    @Throws(Exception::class)
    fun updateProfilePicture() {
    }

}