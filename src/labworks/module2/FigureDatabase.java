package labworks.module2;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class FigureDatabase implements AutoCloseable {
    private final Connection conn;

    private static final String SELECT = "SELECT id, type, width, height, width * height AS area, "
            + "2 * (width + height) AS perimeter FROM figures ";

   
    public FigureDatabase(String url) throws SQLException {
        conn = DriverManager.getConnection(url);
        createSchema();
    }

    private void createSchema() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS figures ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "type TEXT NOT NULL CHECK(type IN ('Rectangle','Square','Figure')), "
                + "width REAL NOT NULL CHECK(width > 0), "
                + "height REAL NOT NULL CHECK(height > 0), "
                + "CHECK(type <> 'Square' OR width = height))";
        try (Statement st = conn.createStatement()) {
            st.execute(sql);
        }
    }

    
    public void recreate() throws SQLException {
        try (Statement st = conn.createStatement()) {
            st.execute("DROP TABLE IF EXISTS figures");
        }
        createSchema();
    }

    private List<Object[]> query(PreparedStatement ps) throws SQLException {
        List<Object[]> rows = new ArrayList<>();
        try (ps; ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                rows.add(new Object[] {
                        rs.getInt("id"), rs.getString("type"),
                        rs.getDouble("width"), rs.getDouble("height"),
                        rs.getDouble("area"), rs.getDouble("perimeter") });
            }
        }
        return rows;
    }


    public List<Object[]> selectAll() throws SQLException {
        return query(conn.prepareStatement(SELECT + "ORDER BY id"));
    }


    public List<Object[]> selectByPerimeter(double max) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(SELECT + "WHERE 2 * (width + height) < ? ORDER BY perimeter");
        ps.setDouble(1, max);
        return query(ps);
    }


    public List<Object[]> selectByType(String type) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(SELECT + "WHERE type = ? ORDER BY id");
        ps.setString(1, type);
        return query(ps);
    }

  
    public void insert(String type, double w, double h) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO figures(type, width, height) VALUES (?, ?, ?)")) {
            ps.setString(1, type);
            ps.setDouble(2, w);
            ps.setDouble(3, h);
            ps.executeUpdate();
        }
    }

 
    public void update(int id, String type, double w, double h) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("UPDATE figures SET type = ?, width = ?, height = ? WHERE id = ?")) {
            ps.setString(1, type);
            ps.setDouble(2, w);
            ps.setDouble(3, h);
            ps.setInt(4, id);
            ps.executeUpdate();
        }
    }

    public boolean delete(int id) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM figures WHERE id = ?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }


    public int[] importFromFile(String file) throws IOException {
        int ok = 0;
        int bad = 0;
        for (String line : Files.readAllLines(Paths.get(file), StandardCharsets.UTF_8)) {
            if (line.trim().isEmpty()) {
                continue;
            }
            try {
                String[] p = line.trim().split(";");
                insert(p[0].trim(), Double.parseDouble(p[1].trim()), Double.parseDouble(p[2].trim()));
                ok++;
            } catch (Exception e) {
                bad++;
            }
        }
        return new int[] { ok, bad };
    }

    @Override
    public void close() throws SQLException {
        conn.close();
    }
}
