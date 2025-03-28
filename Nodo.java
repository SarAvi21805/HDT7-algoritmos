/**
 * @author Alejandra Avilés
 * Clase que representa un nodo en una estructura de árbol
 * Contiene una clave y un valor de tipo Producto
 */
public class Nodo {
    String clave;
    Producto valor;
    Nodo izquierdo, derecho;

    /**
     * Constructor de la clase Nodo
     * 
     * @param clave La clave del nodo
     * @param valor El producto asociado al nodo
     */
    
    public Nodo(String clave, Producto valor) {
        this.clave = clave;
        this.valor = valor;
        this.izquierdo = null;
        this.derecho = null;
    }
}
