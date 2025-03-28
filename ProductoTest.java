import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;

public class ProductoTest {
    private Producto producto;
    private Map<String, Integer> tallas;

    @BeforeEach
    public void setUp() {
        tallas = new HashMap<>();
        tallas.put("S", 10);
        tallas.put("M", 5);
        producto = new Producto("SKU123", "Camiseta", "Camiseta de algodón", tallas);
    }

    @Test
    public void testGetters() {
        assertEquals("SKU123", producto.getSku());
        assertEquals("Camiseta", producto.getNombre());
        assertEquals("Camiseta de algodón", producto.getDescripcion());
        assertEquals(tallas, producto.getTallas());
    }

    @Test
    public void testSetters() {
        producto.setSku("SKU456");
        producto.setNombre("Pantalón");
        producto.setDescripcion("Pantalón de mezclilla");
        
        Map<String, Integer> nuevasTallas = new HashMap<>();
        nuevasTallas.put("L", 7);
        producto.setTallas(nuevasTallas);

        assertEquals("SKU456", producto.getSku());
        assertEquals("Pantalón", producto.getNombre());
        assertEquals("Pantalón de mezclilla", producto.getDescripcion());
        assertEquals(nuevasTallas, producto.getTallas());
    }

    @Test
    public void testActualizarTallas() {
        Map<String, Integer> nuevasTallas = new HashMap<>();
        nuevasTallas.put("S", 5); //Se espera que S se actualice a 15
        nuevasTallas.put("L", 3); //Se espera que L incremente 3
        
        producto.actualizarTallas(nuevasTallas);
        
        assertEquals(5, producto.getTallas().get("S")); // 10 + 5
        assertEquals(3, producto.getTallas().get("L")); // nuevo tamaño
        assertEquals(5, producto.getTallas().get("M")); // sin cambios
    }

    @Test
    public void testAumentarCantidad() {
        producto.aumentarCantidad("S", 5);
        assertEquals(15, producto.getTallas().get("S")); // 10 + 5

        producto.aumentarCantidad("L", 2);
        assertEquals(2, producto.getTallas().get("L")); // nuevo tamaño
    }

    @Test
    public void testRestarCantidad() {
        producto.restarCantidad("S", 3);
        assertEquals(7, producto.getTallas().get("S")); // 10 - 3

        producto.restarCantidad("M", 5);
        assertEquals(0, producto.getTallas().get("M")); // 5 - 5

        // Intentar restar más de lo que hay
        producto.restarCantidad("S", 10); // Debería imprimir un mensaje
        assertEquals(7, producto.getTallas().get("S")); // Sin cambios

        // Intentar restar de una talla que no existe
        producto.restarCantidad("L", 1); // Debería imprimir un mensaje
    }
}