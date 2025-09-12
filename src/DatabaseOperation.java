import java.sql.Connection;
import java.util.Scanner;

public interface DatabaseOperation <T>{//Java Jeneric Concept
        public abstract void insert(Connection con, int courseId) throws Exception;

        public abstract void update(Connection con,Scanner sc) throws Exception;
        public abstract void delete(Connection con,int id) throws Exception;

        // Polymorphic method — each DAO can render/return a different “view”
        public abstract void view(Connection con) throws Exception;
}

