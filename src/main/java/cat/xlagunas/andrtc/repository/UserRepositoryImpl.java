package cat.xlagunas.andrtc.repository;

import cat.xlagunas.andrtc.exception.ExistingUserException;
import cat.xlagunas.andrtc.exception.UserNotFoundException;
import cat.xlagunas.andrtc.model.UserDto;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.*;

public class UserRepositoryImpl implements UserRepository {

    private final JdbcTemplate jdbcTemplate;
    private final UserRowMapper rowMapper;
    private final PasswordEncoder passwordEncoder;

    private final static String FIND_USER_BY_ID = "SELECT * FROM USER WHERE ID = ?";
    private final static String FIND_USER_BY_USERNAME = "SELECT * FROM USER WHERE USERNAME = ?";
    private final static String SEARCH_USERS_BY_USERNAME = "SELECT * FROM USER WHERE USERNAME LIKE ?";
    private final static String UPDATE_PASSWORD = "UPDATE USER SET PASSWORD = ?, LAST_PASSWORD_UPDATE = ? WHERE ID = ?";
    private final static String UPDATE_PROFILE_PIC = "UPDATE USER SET PROFILE_PIC = ? WHERE ID = ?";

    public UserRepositoryImpl(JdbcTemplate template, UserRowMapper rowMapper, PasswordEncoder encoder) {
        this.jdbcTemplate = template;
        this.rowMapper = rowMapper;
        this.passwordEncoder = encoder;
    }

    @Override
    public boolean login(UserDto user) {

        return false;
    }

    @Override
    public long insertUser(UserDto user) throws ExistingUserException {
        try {
            SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
            simpleJdbcInsert.withTableName("USER")
                    .usingGeneratedKeyColumns("ID");
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("ID", user.id);
            parameters.put("EMAIL", user.email);
            parameters.put("USERNAME", user.username);
            parameters.put("FIRST_NAME", user.firstname);
            parameters.put("LAST_NAME", user.lastname);
            parameters.put("PASSWORD", passwordEncoder.encode(user.password));
            parameters.put("PROFILE_PIC", user.profilePic);
            parameters.put("LAST_PASSWORD_UPDATE", Timestamp.from(Calendar.getInstance().toInstant()));

            long key = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
            return key;
        } catch (DuplicateKeyException ex) {
            throw new ExistingUserException("A user with same username already exists in database", ex);
        }
    }

    @Override
    public UserDto findUser(long userId) throws UserNotFoundException {
        try {
            UserDto dto = jdbcTemplate.queryForObject(FIND_USER_BY_ID,
                    new Object[]{userId}, rowMapper.insertUserRowMapper);
            return dto;
        } catch (EmptyResultDataAccessException ex) {
            throw new UserNotFoundException(String.format("User with id %s not found in database", userId), ex);
        }

    }

    @Override
    public UserDto findUser(String username) throws UserNotFoundException {
        try {
            UserDto dto = jdbcTemplate.queryForObject(FIND_USER_BY_USERNAME,
                    new Object[]{username}, rowMapper.insertUserRowMapper);
            return dto;
        } catch (EmptyResultDataAccessException ex) {
            throw new UserNotFoundException(String.format("User with username %s not found in database", username), ex);
        }

    }

    @Override
    public List<UserDto> findUsers(String username) {
        String formattedUsername = new StringBuilder("%")
                .append(username)
                .append("%")
                .toString();
        return jdbcTemplate.query(SEARCH_USERS_BY_USERNAME, new Object[]{formattedUsername}, rowMapper.searchUsersRowMapper);
    }

    @Override
    public Optional<UserDto> findUserOptional(long userId) {
        Optional<UserDto> optionalUser;
        try {
            optionalUser = Optional.of(findUser(userId));
        } catch (UserNotFoundException ex) {
            optionalUser = Optional.empty();
        }

        return optionalUser;
    }

    @Override
    public boolean updatePassword(UserDto user) {
        int affectedRows = jdbcTemplate.update(UPDATE_PASSWORD, passwordEncoder.encode(user.password), Time.from(Calendar.getInstance().toInstant()), user.id);
        return affectedRows > 0;
    }

    @Override
    public boolean updateProfilePic(UserDto user) {
        int affectedRows = jdbcTemplate.update(UPDATE_PROFILE_PIC, user.profilePic, user.id);
        return affectedRows > 0;
    }


}
