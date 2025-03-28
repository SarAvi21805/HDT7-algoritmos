import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * @author Alejandra Avilés
 * Clase principal que gestiona la interacción con el usuario y el inventario de productos
 */
public class Main {
    /**
     * Método principal que inicia la aplicación
     * @param args Argumentos de línea de comandos 
    */
    public static void main(String[] args) {
        BinaryTree arbolSku = new BinaryTree();
        BinaryTree arbolNombre = new BinaryTree();
        Inventario inventario = new Inventario();

        /* Carga de datos desde el CSV */
        Inventario.cargarInventario("inventario_ropa_deportiva_30.csv", arbolSku, arbolNombre);
        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            /* Muestra el menú de opciones */
            System.out.println("Menú de Inventario:");
            System.out.println("1. Listar productos");
            System.out.println("2. Buscar producto por SKU");
            System.out.println("3. Buscar producto por nombre");
            System.out.println("4. Editar producto");
            System.out.println("5. Agregar producto");
            System.out.println("6. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine(); // Limpieza del buffer

            switch (opcion) {
                case 1: /* Listar productos */
                System.out.println("¿Desea listar por SKU o Nombre? (Ingrese el número de la opción) \n 1. SKU\n 2. Nombre\n");
                int tipoListado = scanner.nextInt();
                scanner.nextLine();
                if (tipoListado == 1) {
                    System.out.println("Listado de productos por SKU:");
                    arbolSku.listar(true);
                } else if (tipoListado == 2){
                    System.out.println("Listado de productos por Nombre:");
                    arbolNombre.listar(false);
                } else {
                    System.out.println("Opción inválida. Regresando al menú.");
                }
                break;
            case 2: /* Buscar producto por SKU */
                System.out.print("Ingrese el SKU del producto: ");
                String skuBuscar = scanner.nextLine();
                Producto productoEncontrado = arbolSku.buscar(skuBuscar);
                if (productoEncontrado != null) {
                    System.out.println("Producto encontrado: " + productoEncontrado.getNombre() + ", " + productoEncontrado.getDescripcion() + ", Tallas: " + productoEncontrado.getTallas());
                } else {
                    System.out.println("Producto no encontrado.");
                }
                break;
            case 3: /* Buscar producto por nombre */
                System.out.println("Ingrese el nombre del producto: ");
                String nombreBuscar = scanner.nextLine();
                Producto productoPorNombre = arbolNombre.buscarPorNombre(nombreBuscar);
                if (productoPorNombre != null) {
                    System.out.println("Producto encontrado: " + productoPorNombre.getNombre() + ", " + productoPorNombre.getDescripcion() + ", Tallas: " + productoPorNombre.getTallas());
                } else {
                    System.out.println("Producto no encontrado.");
                }
                break;
            case 4: /* Editar producto */
                System.out.print("Ingrese el SKU del producto a editar: ");
                String skuEditar = scanner.nextLine();
                Producto productoEditar = arbolSku.buscar(skuEditar);
                if (productoEditar != null) {
                    System.out.print("Ingrese la nueva descripción (deje en blanco para no cambiar): ");
                    String nuevaDescripcion = scanner.nextLine();
                    
                    System.out.println("¿Desea añadir o restar productos? (añadir/restar): ");
                    String operacion = scanner.nextLine().toLowerCase();

                    System.out.print("Ingrese las tallas y cantidades (formato talla:cantidad, separado por comas): ");
                    String cantidadesStr = scanner.nextLine();
                    Map<String, Integer> cantidades = new HashMap<>();
                    if (!cantidadesStr.isEmpty()){
                        String[] tallasArray = cantidadesStr.split(",");
                        for (String tallas : tallasArray) {
                            String[] tallaPartes = tallas.split(":");
                            if (tallaPartes.length == 2) {
                                String tallaNombre = tallaPartes[0].trim();
                                int cantidad = Integer.parseInt(tallaPartes[1].trim());
                                cantidades.put(tallaNombre, cantidad);
                            }
                        }
                    
                    /* Actualización del producto */
                    inventario.editarProducto(arbolSku, skuEditar, nuevaDescripcion, operacion, cantidades, "inventario_ropa_deportiva_30.csv");
                    System.out.println("Producto actualizado.");
                    } else {
                    System.out.println("Producto no encontrado.");
                }
            }
                break;
            case 5: /* Agregar producto */
                String ultimoSku = Inventario.obtenerUltimoSku("inventario_ropa_deportiva_30.csv");
                System.out.println("El último SKU registrado es: " + ultimoSku);
                System.out.println("Ingrese el SKU del nuevo producto (siga el formato de " + ultimoSku+ ", considerando que el que usted ingrese debe ser mayor que: "+ ultimoSku +".)");
                String skuNuevo = scanner.nextLine();
                if (Integer.parseInt(skuNuevo) <= Integer.parseInt(ultimoSku)) {
                    System.out.println("El SKU ingresado debe ser mayor que el SKU: " + ultimoSku + ".");
                    return;
                }

                System.out.println("Ingrese el nombre del nuevo producto: ");
                String nuevoNombre = scanner.nextLine();
                if (arbolNombre.buscar(nuevoNombre) != null){
                    System.out.println("El producto con el nombre " + nuevoNombre + " ya existe en el sistema.");
                    return;
                }
                System.out.println("Ingrese la descripción del nuevo producto: ");
                String nuevaDescripcion = scanner.nextLine();
                System.out.println("Ingrese las tallas disponibles (formato talla:cantidad, separado por comas): ");
                String tallasStr = scanner.nextLine();
                Map<String, Integer> tallas = new HashMap<>();
                if (!tallasStr.isEmpty()) {
                    String[] tallasArray = tallasStr.split(",");
                    for (String talla : tallasArray) {
                        String[] tallaPartes = talla.split(":");
                        if (tallaPartes.length == 2) {
                            String tallaNombre = tallaPartes[0].trim();
                            int cantidad = Integer.parseInt(tallaPartes[1].trim());
                            tallas.put(tallaNombre, cantidad);
                        }
                    }
                }
                arbolSku.agregarProducto(arbolSku, arbolNombre, skuNuevo, nuevoNombre, nuevaDescripcion, tallas, "inventario_ropa_deportiva_30.csv");
                System.out.println("Producto "+ skuNuevo + ": " + nuevoNombre + " agregado exitosamente.");
                break;
            case 6: /* Salir */
                System.out.println("Saliendo del programa.");
                break;
            default: /* Opción inválida */
                System.out.println("Opción no válida. Intente de nuevo.");
            }
        } while (opcion != 6);
            scanner.close();
        }
    }
