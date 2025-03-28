import java.util.Map;

/**
 * @author Alejandra Avilés
 * Clase representante de un producto con cada uno de sus caracterísiticas solicitadas.
 */
public class Producto {
    private String sku, nombre, descripcion;
    private Map<String, Integer> tallas;

    /**
     * Constructor de la clase Producto
     * @param sku           El SKU del producto
     * @param nombre        El nombre del producto
     * @param descripcion   La descripción del producto
     * @param tallas        Un mapa que contiene las tallas disponibles y cantidades de las mismas
     */
    public Producto(String sku, String nombre, String descripcion, Map<String, Integer> tallas) {
        this.sku = sku;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tallas = tallas;
    }
    /* Getters */
    /**
     * @param getSku Obtiene el SKU del producto
     * @return El SKU
     * 
     * @param getNombre Obtiene el nombre del producto
     * @return nombre
     * 
     * @param getDescripcion Obtiene la descripción del producto
     * @return descripcion
     * 
     * @param getTallas Obtiene la descripción del producto
     * @return tallas y cantidades
     */
    public String getSku() { return sku; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public Map<String, Integer> getTallas() { return tallas; }

    /* Setters */
        /**
     * @param setSku Establece el SKU del producto
     * 
     * @param setNombre Establece el nombre del producto
     * 
     * @param setDescripcion Establece la descripción del producto
     * 
     * @param setTallas Establece la descripción del producto
     */
    public void setSku(String sku) { this.sku = sku; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setTallas(Map<String, Integer> tallas) { this.tallas = tallas; }

    /** 
     * Actualización de las tallas de productos con nuevas cantidades.
     * 
     * @param nuevasTallas Un mapa de tallas y la cantidad de productos por agregar (actualizar)
    */
    public void actualizarTallas(Map<String, Integer> nuevasTallas) {
        for (Map.Entry<String, Integer> en : nuevasTallas.entrySet()) {
            String talla = en.getKey();
            Integer cantidad = en.getValue();
            tallas.put(talla, cantidad);
        }
    }

    public void aumentarCantidad(String talla, int cantidad) {
        tallas.put(talla, tallas.getOrDefault(talla, 0) + cantidad);
    }
    
    public void restarCantidad(String talla, int cantidad) {
        if (tallas.containsKey(talla)) {
            int nuevaCantidad = tallas.get(talla) - cantidad;
            if (nuevaCantidad < 0) {
                System.out.println("No hay suficiente cantidad para restar de la talla " + talla);
            } else {
                tallas.put(talla, nuevaCantidad);
            }
        } else {
            System.out.println("La talla " + talla + " no existe en el producto.");
        }
    }
    
}