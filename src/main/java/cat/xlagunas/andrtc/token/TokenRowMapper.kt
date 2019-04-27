package cat.xlagunas.andrtc.token

import org.springframework.jdbc.core.RowMapper

object TokenRowMapper {

    private fun mapper(): RowMapper<Token> {
        return RowMapper { rs, _ ->
            Token(id = rs.getLong(rs.findColumn("ID")),
                    platform = rs.getString(rs.findColumn("PLATFORM")),
                    userId = rs.getLong(rs.findColumn("OWNER")),
                    value = rs.getString(rs.findColumn("TOKEN")))
        }
    }


    fun insertMapper(): RowMapper<Token> {
        return mapper()
    }
}