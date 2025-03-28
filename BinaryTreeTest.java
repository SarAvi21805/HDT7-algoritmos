import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;

public class BinaryTreeTest {
    private BinaryTree arbolSku;
    private BinaryTree arbolNombre;
    private Producto producto;

    @BeforeEach
    public void setUp() {
        arbolSku = new BinaryTree();
        arbolNombre = new BinaryTree();
        Map<String, Integer> tallas = new HashMap<>();
        tallas.put("S", 10);
        tallas.put("M", 5);
        producto = new Producto("SKU123", "Camiseta", "Camiseta de algodón", tallas);
    }

    @Test
    public void testInsertarYBuscarPorSku() {
        arbolSku.insertar(producto.getSku(), producto);
        Producto encontrado = arbolSku.buscar(producto.getSku());
        assertNotNull(encontrado);
        assertEquals(producto.getSku(), encontrado.getSku());
    }

    @Test
    public void testInsertarYBuscarPorNombre() {
        arbolNombre.insertar(producto.getNombre(), producto);
        Producto encontrado = arbolNombre.buscarPorNombre(producto.getNombre());
        assertNotNull(encontrado);
        assertEquals(producto.getNombre(), encontrado.getNombre());
    }

    @Test
    public void testAgregarProductoNuevo() {
        arbolSku.agregarProducto(arbolSku, arbolNombre, "SKU123", "Camiseta", "Camiseta de algodón", producto.getTallas(), "inventario.csv");
        assertNotNull(arbolSku.buscar(producto.getSku()));
        assertNotNull(arbolNombre.buscar(producto.getNombre()));
    }

    @Test
public void testAgregarProductoExistentePorSku() {
    // Insertar el producto inicialmente
    arbolSku.insertar(producto.getSku(), producto);
    
    /* Intento de agregar un producto nuevamente (duplicado) */
    arbolSku.agregarProducto(arbolSku, arbolNombre, "SKU123", "Camiseta", "Camiseta de algodón", producto.getTallas(), "inventario.csv");
    
    /* Verificando la cantidad de tallas */
    assertEquals(10, arbolSku.buscar(producto.getSku()).getTallas().get("S")); // Asumiendo que la cantidad inicial era 10
}

@Test
public void testAgregarProductoExistentePorNombre() {
    // Insertar el producto inicialmente
    arbolNombre.insertar(producto.getNombre(), producto);
    
    /* Intento de agregar un producto nuevamente (duplicado) */
    arbolSku.agregarProducto(arbolSku, arbolNombre, "SKU124", "Camiseta", "Camiseta de algodón", producto.getTallas(), "inventario.csv");
    
    /* Verificando la cantidad de prendas */
    assertEquals(10, arbolNombre.buscar(producto.getNombre()).getTallas().get("S")); // Asumiendo que la cantidad inicial era 10
}

    @Test
    public void testListar() {
        arbolSku.insertar(producto.getSku(), producto);
        assertDoesNotThrow(() -> arbolSku.listar());
    }
}
