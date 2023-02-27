package hello.jdbc.repository;

import hello.jdbc.connection.DBConnectionUtil;
import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.NoSuchElementException;

@Slf4j
public class MemberRepositoryV1 {

    private final DataSource dataSource;

    public MemberRepositoryV1(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Member save(Member member) throws SQLException {
        String sql = "INSERT INTO member(member_id, money) VALUES (?, ?)";

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, member.getMemberId());
            preparedStatement.setInt(2, member.getMoney());
            preparedStatement.executeUpdate();
            return member;
        } catch (SQLException e) {
            log.error("db error", e);
            throw new RuntimeException(e);
        } finally {
            close(connection, preparedStatement, null);
        }

    }

    public Member findById(String memberId) throws SQLException {
        String sql = "SELECT * FROM member WHERE member_id = ?";

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, memberId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Member member = new Member();
                member.setMemberId(resultSet.getString("member_id"));
                member.setMoney(resultSet.getInt("money"));
                return member;
            } else {
                throw new NoSuchElementException("member not found");
            }
        } catch (SQLException e) {
            log.error("db error", e);
            throw new RuntimeException(e);
        } finally {
            close(connection, preparedStatement, null);
        }

    }


    public void update(String memberId, int money) throws SQLException {
        String sql = "update member set money =? where member_Id =?";


        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, money);
            preparedStatement.setString(2, memberId);

            int resultSize = preparedStatement.executeUpdate();
            log.info("resultSize ={}", resultSize);

        } catch (SQLException e) {
            log.error("db error", e);
            throw new RuntimeException(e);
        } finally {
            close(connection, preparedStatement, null);
        }

    }

    public void delete(String memberId) throws SQLException {
        String sql = "delete from member where member_id=?";


        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, memberId);

            int resultSize = preparedStatement.executeUpdate();
            log.info("resultSize ={}", resultSize);

        } catch (SQLException e) {
            log.error("db error", e);
            throw new RuntimeException(e);
        } finally {
            close(connection, preparedStatement, null);
        }
    }


    private void close(Connection connection, Statement statement, ResultSet resultSet) throws SQLException {
        JdbcUtils.closeResultSet(resultSet);
        JdbcUtils.closeStatement(statement);
        JdbcUtils.closeConnection(connection);
    }

    private Connection getConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        log.info("get connection ={}, class={}", dataSource, dataSource.getConnection());
        return connection;
    }
}
