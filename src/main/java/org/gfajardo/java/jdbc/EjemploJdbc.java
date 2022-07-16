package org.gfajardo.java.jdbc;

import org.gfajardo.java.jdbc.model.Producto;
import org.gfajardo.java.jdbc.repositorio.ProductoRepositorioImp;
import org.gfajardo.java.jdbc.repositorio.Repositorio;
import org.gfajardo.java.jdbc.util.ConexionBaseDatos;

import java.sql.*;
import java.util.Date;

public class EjemploJdbc {
    public static void main(String[] args) throws SQLException {


        try (
                Connection conn = ConexionBaseDatos.getInstance();
        ) {
            Repositorio<Producto> repositorio=new ProductoRepositorioImp();
            System.out.println("======LISTADO DE PRODUCTOS======");
            System.out.println(" ");
            repositorio.listar().forEach(System.out::println);
            System.out.println(" ");
            System.out.println("======BUSQUEDA POR ID======");
            System.out.println(" ");
            System.out.println(repositorio.porId(2L));
            System.out.println(" ");
            /*System.out.println("======GUARDAR======");
            Producto prod1=new Producto(null,"xiaomi redmi note 9",250, new Date());
            repositorio.guardar(prod1);
            System.out.println(" ");
            repositorio.listar().forEach(System.out::println);
            System.out.println(" ");
            System.out.println("======ACTUALIZAR======");
            prod1.setId(6L);
            prod1.setPrecio(450);
            prod1.setNombre("samsung s22 ultra");
            repositorio.guardar(prod1);
            System.out.println(" ");
            repositorio.listar().forEach(System.out::println);*/
            System.out.println("======ELIMINAR======");
//            repositorio.eliminar(1L);
            repositorio.listar().forEach(System.out::println);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
