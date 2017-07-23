package cat.xlagunas.andrtc.repository;

import cat.xlagunas.andrtc.exception.UserNotFoundException;
import cat.xlagunas.andrtc.model.UserDto;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class UserRepository {
    private final JdbcTemplate jdbcTemplate;
    private final UserRowMapper rowMapper;
    private final static String INSERT_USER = "INSERT INTO USER (ID, EMAIL, USERNAME, FIRST_NAME, LAST_NAME, PASSWORD, PROFILE_PIC) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?)";
    private final static String UPDATE_PASSWORD = "UPDATE USER SET PASSWORD = ? WHERE ID = ?";
    private final static String UPDATE_PROFILE_PIC = "UPDATE USER SET PROFILE_PIC = ? WHERE ID = ?";

    public UserRepository(JdbcTemplate template, UserRowMapper rowMapper) {
        this.jdbcTemplate = template;
        this.rowMapper = rowMapper;
    }

    public long insertUser(UserDto user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        simpleJdbcInsert.withTableName("USER")
                .usingGeneratedKeyColumns("ID");
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("ID", user.id);
        parameters.put("EMAIL", user.email);
        parameters.put("USERNAME", user.username);
        parameters.put("FIRST_NAME", user.firstname);
        parameters.put("LAST_NAME", user.lastname);
        parameters.put("PASSWORD", user.password);
        parameters.put("PROFILE_PIC", user.profilePic);

        long key = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
        return key;
    }

    public UserDto findUser(long userId) throws UserNotFoundException {
        try {
            UserDto dto = jdbcTemplate.queryForObject("SELECT * FROM USER WHERE USER.ID = ?",
                    new Object[]{userId}, rowMapper.insertUserRowMapper);
            return dto;
        } catch (EmptyResultDataAccessException ex) {
            throw new UserNotFoundException(String.format("User with id %s not found in database", userId));
        }

    }

    public Optional<UserDto> findUserOptional(long userId) {
        Optional<UserDto> optionalUser;
        try {
            optionalUser = Optional.of(findUser(userId));
        } catch (UserNotFoundException ex) {
            optionalUser = Optional.empty();
        }

        return optionalUser;
    }

    public boolean updatePassword(UserDto user) {

        int affectedRows = jdbcTemplate.update(UPDATE_PASSWORD, user.password, user.id);
        return affectedRows > 0;
    }

    public boolean updateProfilePic(UserDto user) {
        int affectedRows = jdbcTemplate.update(UPDATE_PROFILE_PIC, user.profilePic, user.id);
        return affectedRows > 0;
    }


}
