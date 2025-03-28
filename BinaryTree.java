import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Map;

/**
 * @author Alejandra Avilés
 * Clase que representa un árbol binario para el almacenamiento de nodos con productos.
 * Permite insertar, buscar y listar productos
 */
public class BinaryTree {
    private Nodo raiz;

    /**
     * Inserta un nuevo producto en el árbol usando una clave
     * @param clave La clave del producto (SKU o nombre)
     * @param valor El producto a insertar
     */
    public void insertar(String clave, Producto valor) {
        raiz = insertarRecursivo(raiz, clave, valor);
    }

    /**
     * Método recursivo para insertar un nuevo nodo en el árbol
     * @param nodo El nodo actual en el que se inserta
     * @param clave La clave del producto
     * @param valor El producto a insertar
     * @return El nodo actualizado
     */
    private Nodo insertarRecursivo(Nodo nodo, String clave, Producto valor) {
        if (nodo == null) { return new Nodo(clave, valor); }
        if (clave.compareTo(nodo.clave) < 0) {
            nodo.izquierdo = insertarRecursivo(nodo.izquierdo, clave, valor);
        } else if (clave.compareTo(nodo.clave) > 0) {
            nodo.derecho = insertarRecursivo(nodo.derecho, clave, valor);
        }
        return nodo;
    }

    /**
     * Búsqueda de productos en el árbol
     * @param clave La clave del producto a buscar
     * @return El producto encontrado o null si no existe
     */
    public Producto buscar(String clave) {
        return buscarRecursivo(raiz, clave);
    }

    /**
     * Método recursivo para buscar un producto en el árbol
     * @param nodo El nodo actual en el que se busca
     * @param clave La clave del producto a buscar
     * @return El producto encontrado o null si no existe
     */
    private Producto buscarRecursivo(Nodo nodo, String clave) {
        if (nodo == null) { return null; }
        if (clave.equals(nodo.clave)) { return nodo.valor; }
        return clave.compareTo(nodo.clave) < 0 ? buscarRecursivo(nodo.izquierdo, clave) : buscarRecursivo(nodo.derecho, clave);
    }

    /**
     * Lista todos los productos en el árbol en orden, según el tipo de clave.
     * @param porSku true para listar por SKU, false para listar por nombre
     */
    public void listar(boolean porSku) {
        if (porSku) {
            listarPorSkuRecursivo(raiz);
        } else {
            listarPorNombreRecursivo(raiz);
        }
    }

    /**
     * Método recursivo para listar los productos del árbol por SKU
     * @param nodo El nodo actual que se está listando
     */
    private void listarPorSkuRecursivo(Nodo nodo) {
        if (nodo != null) {
            listarPorSkuRecursivo(nodo.izquierdo);
            System.out.println("SKU: " + nodo.valor.getSku() + "\nNombre: " + nodo.valor.getNombre());
            listarPorSkuRecursivo(nodo.derecho);
        }
    }

    /**
     * Método recursivo para listar los productos del árbol por nombre
     * @param nodo El nodo actual que se está listando
     */
    private void listarPorNombreRecursivo(Nodo nodo) {
        if (nodo != null) {
            listarPorNombreRecursivo(nodo.izquierdo);
            System.out.println("Nombre: " + nodo.valor.getNombre() + "\nSKU: " + nodo.valor.getSku());
            listarPorNombreRecursivo(nodo.derecho);
        }
    }
    /**
     * Agrega un nuevo producto al árbol y guarda el inventario en el archivo
     * @param arbolSku El árbol binario que almacena productos por SKU
     * @param arbolNombre El árbol binario que almacena productos por Nombre
     * @param sku El SKU del nuevo producto
     * @param nombre El nombre del nuevo producto
     * @param descripcion La descripción del nuevo producto
     * @param tallas Un mapa de tallas y cantidades del nuevo producto
     * @param archivo Ruta del CSV que guarda el inventario (actualizado)
     */
    public void agregarProducto(BinaryTree arbolSku, BinaryTree arbolNombre, String sku, String nombre, String descripcion, Map<String, Integer> tallas, String archivo){
        if(arbolSku.buscar(sku) != null) {
            System.out.println("El producto con el SKU " + sku + " ya existe en el sistema.");
            return;
        }
        if(arbolNombre.buscar(nombre) != null) {
            System.out.println("El producto con el nombre " + nombre + " ya existe en el sistema.");
            return;
        }

        Producto nuevoProducto = new Producto(sku, nombre, descripcion, tallas);
        arbolSku.insertar(sku, nuevoProducto);
        arbolNombre.insertar(nombre, nuevoProducto);

        Inventario.guardarInventario(archivo, arbolSku);
    }

    /**
     * Busca un producto en el árbol por medio del nombre
     * @param nombre El nombre del producto requerido
     * @return El producto encontrado o null si no existe
     */
    public Producto buscarPorNombre(String nombre){
        return buscarPorNombreRecursivo(raiz, nombre.toLowerCase());
    }

    /**
     *  Método recursivo para buscar un producto en el árbol por medio del nombre
     * @param nodo El nodo actual en el que se busca
     * @param nombre El nombre del producto a buscar
     * @return El producto encontrado o null si no existe
     */
    private Producto buscarPorNombreRecursivo(Nodo nodo, String nombre) {
        if (nodo == null) { return null; }
        if (nombre.equalsIgnoreCase(nodo.valor.getNombre())) { return nodo.valor; }
        Producto encontrado = buscarPorNombreRecursivo(nodo.izquierdo, nombre);
        if(encontrado != null) { return encontrado;
        }
        return buscarPorNombreRecursivo(nodo.derecho, nombre);
    }

    /**
     * Lista todos los productos en el árbol y los guarda en un archivo
     * @param bw El BufferedWriter usado para escribir en el archivo
     * @throws IOException Por si ocurre un error al escribir en el archivo
     */
    public void listarParaGuardar(BufferedWriter bw) throws IOException {
        listarParaGuardarRecursivo(raiz, bw);
    }

    /**
     * Método recursivo para listar los productos del árbol y escribirlos en el archivo
     * @param nodo El nodo actual que se está listando
     * @param bw El BufferedWriter usado para escribir en el archivo
     * @throws IOException Por si ocurre un error al escribir en el archivo
     */
    private void listarParaGuardarRecursivo(Nodo nodo, BufferedWriter bw) throws IOException {
        if (nodo != null) {
            listarParaGuardarRecursivo(nodo.izquierdo, bw);
            String tallasStr = nodo.valor.getTallas().entrySet().stream()
            .map(entry -> entry.getKey() + ":" + entry.getValue())
            .reduce((talla1, talla2) -> talla1 + "|" + talla2).orElse("");
            bw.write(nodo.valor.getSku() + "," + nodo.valor.getNombre() + "," + nodo.valor.getDescripcion() + "," + tallasStr + "\n");
            listarParaGuardarRecursivo(nodo.derecho, bw);
        }
    }
}