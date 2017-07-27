package cat.xlagunas.andrtc.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;

@JsonDeserialize(builder = UserDto.Builder.class)
public class UserDto implements UserDetails {
    public final long id;
    public final String username;
    public final String firstname;
    public final String lastname;
    @JsonIgnore
    public final String password;
    public final String email;
    public final String profilePic;
    @JsonIgnore
    public final Instant passwordUpdate;

    private UserDto(Builder builder) {
        this.id = builder.id;
        this.username = builder.username;
        this.firstname = builder.firstname;
        this.lastname = builder.lastname;
        this.password = builder.password;
        this.profilePic = builder.profilePic;
        this.email = builder.email;
        this.passwordUpdate = builder.passwordUpdate;
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return username;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }

    @JsonPOJOBuilder(buildMethodName = "build")
    public static class Builder {
        private long id;
        private String username;
        private String firstname;
        private String lastname;
        private String password;
        private String profilePic;
        private String email;
        private Instant passwordUpdate;

        @JsonSetter(value = "id")
        public Builder id(long id) {
            this.id = id;
            return this;
        }

        @JsonSetter(value = "username")
        public Builder username(String username) {
            this.username = username;
            return this;
        }

        @JsonSetter(value = "firstname")
        public Builder firstname(String firstname) {
            this.firstname = firstname;
            return this;
        }

        @JsonSetter(value = "email")
        public Builder email(String email) {
            this.email = email;
            return this;
        }

        @JsonSetter(value = "lastname")
        public Builder lastname(String lastname) {
            this.lastname = lastname;
            return this;
        }

        @JsonSetter(value = "password")
        public Builder password(String password) {
            this.password = password;
            return this;
        }

        @JsonSetter(value = "profilePic")
        public Builder profilePic(String profilePic) {
            this.profilePic = profilePic;
            return this;
        }

        public Builder passwordUpdate(Instant date) {
            this.passwordUpdate = date;
            return this;
        }

        public UserDto build() {
            return new UserDto(this);
        }
    }
}
