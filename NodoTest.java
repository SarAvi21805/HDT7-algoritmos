import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class NodoTest {
    private Producto producto;
    private Nodo nodo;

    @BeforeEach
    public void setUp() {
        producto = new Producto("SKU123", "Camiseta", "Camiseta de algodón", null);
        nodo = new Nodo("clave1", producto);
    }

    @Test
    public void testNodo() {
        assertEquals("clave1", nodo.clave);
        assertEquals(producto, nodo.valor);
        assertNull(nodo.izquierdo);
        assertNull(nodo.derecho);
    }
}