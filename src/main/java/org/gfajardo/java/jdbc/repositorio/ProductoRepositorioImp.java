package org.gfajardo.java.jdbc.repositorio;

import org.gfajardo.java.jdbc.model.Categoria;
import org.gfajardo.java.jdbc.model.Producto;
import org.gfajardo.java.jdbc.util.ConexionBaseDatos;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoRepositorioImp implements Repositorio<Producto> {

    private Connection getConnection() throws SQLException {
        return ConexionBaseDatos.getInstance();
    }


    @Override
    public List<Producto> listar() {
        List<Producto> productos = new ArrayList<>();
        try (
                Statement stmt = getConnection().createStatement();
                ResultSet result = stmt.executeQuery("SELECT p.*,c.nombre as categoria FROM productos as p INNER JOIN categorias as c ON (p.categoria_id=c.id)");
        ) {
            while (result.next()) {
                Producto p = createProduct(result);
                productos.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productos;
    }


    @Override
    public Producto porId(Long id) {
        Producto producto = null;
        try (PreparedStatement stmt = getConnection()
                .prepareStatement("SELECT p.*,c.nombre as categoria FROM productos as p INNER JOIN categorias as c ON (p.categoria_id=c.id) WHERE p.id=?");
        ) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    producto = createProduct(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return producto;
    }

    @Override
    public void guardar(Producto producto) {
        String sql;
        if (producto.getId() != null) {
            sql = "UPDATE productos SET nombre=?,precio=? ,categoria_id=? WHERE id=?";
        } else {
            sql = "INSERT INTO productos(nombre,precio,categoria_id,fecha_registro) VALUES (?,?,?,?)";
        }
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, producto.getNombre());
            stmt.setInt(2, producto.getPrecio());
            stmt.setLong(3,producto.getCategoria().getId());
            if (producto.getId() != null && producto.getId() > 0) {
                stmt.setLong(4, producto.getId());
            } else {
                stmt.setDate(4, new Date(producto.getFechaRegistro().getTime()));
            }

            stmt.executeUpdate();
            if (producto.getId() != null) {
                System.out.println("El producto se actualizo correcatamente");
            } else {
                System.out.println("El producto se creo correctamente");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void eliminar(Long id) {
        try (PreparedStatement stmt = getConnection().prepareStatement("DELETE from productos WHERE id=?")) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
            System.out.println("El producto se elimino correctamente");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Producto createProduct(ResultSet result) throws SQLException {
        Producto p = new Producto();
        p.setId(result.getLong("id"));
        p.setNombre(result.getString("nombre"));
        p.setPrecio(result.getInt("precio"));
        p.setFechaRegistro(result.getDate("fecha_registro"));
        Categoria categoria =new Categoria();
        categoria.setId(result.getLong("categoria_id"));
        categoria.setNombre(result.getString("categoria"));
        p.setCategoria(categoria);
        return p;
    }
}
