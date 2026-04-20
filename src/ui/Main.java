package cr.ac.ucenfotec.ui;

import cr.ac.ucenfotec.bl.entities.clinica.Clinica;
import cr.ac.ucenfotec.bl.logic.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Punto de entrada y capa de interfaz de usuario (UI) del sistema de citas médicas.
 *
 * <p><b>Concepto POO — Arquitectura en capas:</b> Esta clase pertenece exclusivamente
 * a la capa de <em>Interfaz de Usuario (UI)</em>. Su única responsabilidad es capturar
 * la entrada del usuario, mostrar resultados en pantalla y delegar toda la lógica
 * de negocio al {@link Service}. Nunca manipula directamente las entidades del dominio.</p>
 *
 * <p><b>Concepto POO — Dependencia:</b> {@code Main} depende de {@link Service}
 * para funcionar. Esta dependencia es unidireccional: la UI conoce al servicio,
 * pero el servicio no conoce a la UI.</p>
 *
 */
public class Main {


    // Constantes de formato de pantalla
    private static final String LINEA_SEPARADORA  = "═".repeat(55);
    private static final String LINEA_SIMPLE      = "─".repeat(55);
    private static final String FORMATO_FECHA     = "dd/MM/yyyy";
    private static final String FORMATO_FECHA_HORA= "dd/MM/yyyy HH:mm";


    /**
     * Método principal del programa.
     * Inicializa la clínica, el servicio y lanza el menú principal.
     *
     * @param argumentos Argumentos de línea de comandos (no utilizados).
     */
    public static void main(String[] argumentos) {

        // Inicializar la clínica y el servicio (CAPA BL)
        Clinica clinicaCentral = new Clinica(
                "Clínica Santa Fe",
                "Cartago, Costa Rica",
                "2222-3333"
        );
        Service servicio = new Service(clinicaCentral);

        // Cargar datos de prueba para demostrar el sistema
        cargarDatosDemostracion(servicio);

        // Iniciar el menú principal
        Scanner lectorEntrada = new Scanner(System.in);
        mostrarMenuPrincipal(servicio, lectorEntrada);

        lectorEntrada.close();
        System.out.println("\n¡Hasta luego! Sistema de Citas cerrado.");
    }



    // Menú principal

    /**
     * Despliega el menú principal y gestiona la navegación entre submenús.
     *
     * @param servicio      Servicio de lógica de negocio activo.
     * @param lectorEntrada
     */
    private static void mostrarMenuPrincipal(Service servicio, Scanner lectorEntrada) {

        int opcionSeleccionada;

        do {
            imprimirEncabezado(servicio.getNombreClinica());
            System.out.println("  1. Gestión de Médicos");
            System.out.println("  2. Gestión de Pacientes");
            System.out.println("  3. Gestión de Citas");
            System.out.println("  4. Ver resumen de la clínica");
            System.out.println("  0. Salir");
            System.out.println(LINEA_SEPARADORA);

            opcionSeleccionada = leerEnteroConValidacion(lectorEntrada, "Seleccione una opción: ", 0, 4);

            switch (opcionSeleccionada) {
                case 1 -> mostrarMenuMedicos(servicio, lectorEntrada);
                case 2 -> mostrarMenuPacientes(servicio, lectorEntrada);
                case 3 -> mostrarMenuCitas(servicio, lectorEntrada);
                case 4 -> mostrarResumenClinica(servicio);
                case 0 -> System.out.println("\nCerrando sesión...");
            }

        } while (opcionSeleccionada != 0);
    }


    // Submenú: Médicos

    /**
     * Gestiona las opciones del submenú de médicos.
     *
     * @param servicio      Servicio de lógica de negocio activo.
     * @param lectorEntrada Scanner para leer la entrada del usuario.
     */
    private static void mostrarMenuMedicos(Service servicio, Scanner lectorEntrada) {

        int opcionSeleccionada;

        do {
            imprimirEncabezado("Gestión de Médicos");
            System.out.println("  1. Registrar médico");
            System.out.println("  2. Listar todos los médicos");
            System.out.println("  3. Listar médicos (resumen)");
            System.out.println("  0. Volver al menú principal");
            System.out.println(LINEA_SEPARADORA);

            opcionSeleccionada = leerEnteroConValidacion(lectorEntrada, "Seleccione una opción: ", 0, 3);

            switch (opcionSeleccionada) {
                case 1 -> registrarMedico(servicio, lectorEntrada);
                case 2 -> listarMedicos(servicio);
                case 3 -> listarMedicosResumido(servicio);
                case 0 -> System.out.println("Volviendo al menú principal...\n");
            }

        } while (opcionSeleccionada != 0);
    }

    /**
     * Captura los datos del formulario de registro de médico y los envía al servicio.
     *
     * @param servicio      Servicio de lógica de negocio activo.
     * @param lectorEntrada Scanner para leer la entrada del usuario.
     */
    private static void registrarMedico(Service servicio, Scanner lectorEntrada) {
        System.out.println("\n" + LINEA_SIMPLE);
        System.out.println("  REGISTRAR NUEVO MÉDICO");
        System.out.println(LINEA_SIMPLE);

        System.out.print("  Código médico        : ");
        String codigoMedico = lectorEntrada.nextLine().trim();

        System.out.print("  Nombre               : ");
        String nombreMedico = lectorEntrada.nextLine().trim();

        System.out.print("  Apellido             : ");
        String apellidoMedico = lectorEntrada.nextLine().trim();

        System.out.print("  Número de cédula     : ");
        String cedulaMedico = lectorEntrada.nextLine().trim();

        System.out.print("  Número de celular    : ");
        String celularMedico = lectorEntrada.nextLine().trim();

        System.out.print("  Correo electrónico   : ");
        String correoMedico = lectorEntrada.nextLine().trim();

        double salarioMedico = leerDoubleConValidacion(lectorEntrada,
                "  Salario mensual (₡)  : ");

        System.out.print("  Código especialidad  : ");
        String codigoEspecialidad = lectorEntrada.nextLine().trim();

        System.out.print("  Nombre especialidad  : ");
        String nombreEspecialidad = lectorEntrada.nextLine().trim();

        System.out.print("  Descripción          : ");
        String descripcionEspecialidad = lectorEntrada.nextLine().trim();

        // Delegar al servicio (capa BL) — la UI nunca crea entidades directamente
        String mensajeResultado = servicio.registrarMedico(
                nombreMedico, apellidoMedico, cedulaMedico, celularMedico,
                correoMedico, codigoMedico, salarioMedico,
                codigoEspecialidad, nombreEspecialidad, descripcionEspecialidad);

        System.out.println("\n  >> " + mensajeResultado);
        pausar(lectorEntrada);
    }

    /**
     * Muestra la lista completa de médicos registrados con todos sus datos.
     *
     * @param servicio Servicio de lógica de negocio activo.
     */
    private static void listarMedicos(Service servicio) {
        System.out.println("\n" + LINEA_SIMPLE);
        System.out.println("  LISTA DE MÉDICOS (" + servicio.getCantidadMedicos() + " registrados)");
        System.out.println(LINEA_SIMPLE);

        ArrayList<String> listaMedicos = servicio.getListaMedicosFormateada();
        if (listaMedicos.isEmpty()) {
            System.out.println("  No hay médicos registrados.");
        } else {
            for (String datosMedico : listaMedicos) {
                System.out.println(datosMedico);
                System.out.println(LINEA_SIMPLE);
            }
        }
    }

    /**
     * Muestra la lista resumida de médicos (una línea por médico).
     * Demuestra el polimorfismo de la interfaz {@code IValidable#obtenerDescripcion()}.
     *
     * @param servicio Servicio de lógica de negocio activo.
     */
    private static void listarMedicosResumido(Service servicio) {
        System.out.println("\n" + LINEA_SIMPLE);
        System.out.println("  MÉDICOS — VISTA RESUMIDA");
        System.out.println(LINEA_SIMPLE);

        ArrayList<String> listaMedicosResumida = servicio.getListaMedicosResumida();
        if (listaMedicosResumida.isEmpty()) {
            System.out.println("  No hay médicos registrados.");
        } else {
            for (String descripcionMedico : listaMedicosResumida) {
                System.out.println("  • " + descripcionMedico);
            }
        }
        System.out.println();
    }


    // Submenú: Pacientes
    /**
     * Gestiona las opciones del submenú de pacientes.
     *
     * @param servicio      Servicio de lógica de negocio activo.
     * @param lectorEntrada Scanner para leer la entrada del usuario.
     */
    private static void mostrarMenuPacientes(Service servicio, Scanner lectorEntrada) {

        int opcionSeleccionada;

        do {
            imprimirEncabezado("Gestión de Pacientes");
            System.out.println("  1. Registrar paciente");
            System.out.println("  2. Listar todos los pacientes");
            System.out.println("  3. Listar pacientes (resumen)");
            System.out.println("  0. Volver al menú principal");
            System.out.println(LINEA_SEPARADORA);

            opcionSeleccionada = leerEnteroConValidacion(lectorEntrada, "Seleccione una opción: ", 0, 3);

            switch (opcionSeleccionada) {
                case 1 -> registrarPaciente(servicio, lectorEntrada);
                case 2 -> listarPacientes(servicio);
                case 3 -> listarPacientesResumido(servicio);
                case 0 -> System.out.println("Volviendo al menú principal...\n");
            }

        } while (opcionSeleccionada != 0);
    }

    /**
     * Captura los datos del formulario de registro de paciente y los envía al servicio.
     *
     * @param servicio      Servicio de lógica de negocio activo.
     * @param lectorEntrada Scanner para leer la entrada del usuario.
     */
    private static void registrarPaciente(Service servicio, Scanner lectorEntrada) {
        System.out.println("\n" + LINEA_SIMPLE);
        System.out.println("  REGISTRAR NUEVO PACIENTE");
        System.out.println(LINEA_SIMPLE);

        System.out.print("  Código paciente      : ");
        String codigoPaciente = lectorEntrada.nextLine().trim();

        System.out.print("  Nombre               : ");
        String nombrePaciente = lectorEntrada.nextLine().trim();

        System.out.print("  Apellido             : ");
        String apellidoPaciente = lectorEntrada.nextLine().trim();

        System.out.print("  Número de cédula     : ");
        String cedulaPaciente = lectorEntrada.nextLine().trim();

        System.out.print("  Número de celular    : ");
        String celularPaciente = lectorEntrada.nextLine().trim();

        System.out.print("  Correo electrónico   : ");
        String correoPaciente = lectorEntrada.nextLine().trim();

        LocalDate fechaNacimientoPaciente = leerFechaConValidacion(
                lectorEntrada, "  Fecha de nacimiento (dd/MM/yyyy): ");

        System.out.print("  Tipo de sangre       : ");
        String tipoSangrePaciente = lectorEntrada.nextLine().trim();

        String mensajeResultado = servicio.registrarPaciente(
                nombrePaciente, apellidoPaciente, cedulaPaciente, celularPaciente,
                correoPaciente, codigoPaciente, fechaNacimientoPaciente, tipoSangrePaciente);

        System.out.println("\n  >> " + mensajeResultado);
        pausar(lectorEntrada);
    }

    /**
     * Muestra la lista completa de pacientes con todos sus datos.
     *
     * @param servicio Servicio de lógica de negocio activo.
     */
    private static void listarPacientes(Service servicio) {
        System.out.println("\n" + LINEA_SIMPLE);
        System.out.println("  LISTA DE PACIENTES (" + servicio.getCantidadPacientes() + " registrados)");
        System.out.println(LINEA_SIMPLE);

        ArrayList<String> listaPacientes = servicio.getListaPacientesFormateada();
        if (listaPacientes.isEmpty()) {
            System.out.println("  No hay pacientes registrados.");
        } else {
            for (String datosPaciente : listaPacientes) {
                System.out.println(datosPaciente);
                System.out.println(LINEA_SIMPLE);
            }
        }
    }

    /**
     * Muestra la lista resumida de pacientes (una línea por paciente).
     *
     * @param servicio Servicio de lógica de negocio activo.
     */
    private static void listarPacientesResumido(Service servicio) {
        System.out.println("\n" + LINEA_SIMPLE);
        System.out.println("  PACIENTES — VISTA RESUMIDA");
        System.out.println(LINEA_SIMPLE);

        ArrayList<String> listaPacientesResumida = servicio.getListaPacientesResumida();
        if (listaPacientesResumida.isEmpty()) {
            System.out.println("  No hay pacientes registrados.");
        } else {
            for (String descripcionPaciente : listaPacientesResumida) {
                System.out.println("  • " + descripcionPaciente);
            }
        }
        System.out.println();
    }


    // Submenú: Citas
    /**
     * Gestiona las opciones del submenú de citas médicas.
     *
     * @param servicio      Servicio de lógica de negocio activo.
     * @param lectorEntrada Scanner para leer la entrada del usuario.
     */
    private static void mostrarMenuCitas(Service servicio, Scanner lectorEntrada) {

        int opcionSeleccionada;

        do {
            imprimirEncabezado("Gestión de Citas");
            System.out.println("  1. Programar cita");
            System.out.println("  2. Confirmar cita");
            System.out.println("  3. Cancelar cita");
            System.out.println("  4. Emitir receta (atender cita)");
            System.out.println("  5. Listar todas las citas");
            System.out.println("  6. Listar citas (resumen)");
            System.out.println("  0. Volver al menú principal");
            System.out.println(LINEA_SEPARADORA);

            opcionSeleccionada = leerEnteroConValidacion(lectorEntrada, "Seleccione una opción: ", 0, 6);

            switch (opcionSeleccionada) {
                case 1 -> programarCita(servicio, lectorEntrada);
                case 2 -> confirmarCita(servicio, lectorEntrada);
                case 3 -> cancelarCita(servicio, lectorEntrada);
                case 4 -> emitirReceta(servicio, lectorEntrada);
                case 5 -> listarCitas(servicio);
                case 6 -> listarCitasResumido(servicio);
                case 0 -> System.out.println("Volviendo al menú principal...\n");
            }

        } while (opcionSeleccionada != 0);
    }

    /**
     * Captura los datos para programar una nueva cita médica.
     *
     * @param servicio      Servicio de lógica de negocio activo.
     * @param lectorEntrada Scanner para leer la entrada del usuario.
     */
    private static void programarCita(Service servicio, Scanner lectorEntrada) {
        System.out.println("\n" + LINEA_SIMPLE);
        System.out.println("  PROGRAMAR NUEVA CITA");
        System.out.println(LINEA_SIMPLE);

        // Mostrar médicos y pacientes disponibles para ayudar al usuario
        System.out.println("  Médicos disponibles:");
        for (String descripcionMedico : servicio.getListaMedicosResumida()) {
            System.out.println("    " + descripcionMedico);
        }
        System.out.println("  Pacientes disponibles:");
        for (String descripcionPaciente : servicio.getListaPacientesResumida()) {
            System.out.println("    " + descripcionPaciente);
        }
        System.out.println(LINEA_SIMPLE);

        System.out.print("  Código de la cita    : ");
        String codigoCita = lectorEntrada.nextLine().trim();

        System.out.print("  Motivo de consulta   : ");
        String motivoConsulta = lectorEntrada.nextLine().trim();

        LocalDateTime fechaHoraCita = leerFechaHoraConValidacion(
                lectorEntrada, "  Fecha y hora (dd/MM/yyyy HH:mm): ");

        System.out.print("  Código del médico    : ");
        String codigoMedico = lectorEntrada.nextLine().trim();

        System.out.print("  Código del paciente  : ");
        String codigoPaciente = lectorEntrada.nextLine().trim();

        String mensajeResultado = servicio.programarCita(
                codigoCita, motivoConsulta, fechaHoraCita, codigoMedico, codigoPaciente);

        System.out.println("\n  >> " + mensajeResultado);
        pausar(lectorEntrada);
    }

    /**
     * Captura el código de cita para confirmarla.
     *
     * @param servicio      Servicio de lógica de negocio activo.
     * @param lectorEntrada Scanner para leer la entrada del usuario.
     */
    private static void confirmarCita(Service servicio, Scanner lectorEntrada) {
        System.out.println("\n" + LINEA_SIMPLE);
        System.out.println("  CONFIRMAR CITA");
        System.out.println(LINEA_SIMPLE);

        System.out.print("  Código de la cita    : ");
        String codigoCita = lectorEntrada.nextLine().trim();

        String mensajeResultado = servicio.confirmarCita(codigoCita);
        System.out.println("  >> " + mensajeResultado);
        pausar(lectorEntrada);
    }

    /**
     * Captura el código de cita para cancelarla.
     *
     * @param servicio      Servicio de lógica de negocio activo.
     * @param lectorEntrada Scanner para leer la entrada del usuario.
     */
    private static void cancelarCita(Service servicio, Scanner lectorEntrada) {
        System.out.println("\n" + LINEA_SIMPLE);
        System.out.println("  CANCELAR CITA");
        System.out.println(LINEA_SIMPLE);

        System.out.print("  Código de la cita    : ");
        String codigoCita = lectorEntrada.nextLine().trim();

        String mensajeResultado = servicio.cancelarCita(codigoCita);
        System.out.println("  >> " + mensajeResultado);
        pausar(lectorEntrada);
    }

    /**
     * Captura los datos para emitir una receta y marcar la cita como atendida.
     *
     * @param servicio      Servicio de lógica de negocio activo.
     * @param lectorEntrada Scanner para leer la entrada del usuario.
     */
    private static void emitirReceta(Service servicio, Scanner lectorEntrada) {
        System.out.println("\n" + LINEA_SIMPLE);
        System.out.println("  EMITIR RECETA");
        System.out.println(LINEA_SIMPLE);

        System.out.print("  Código de la cita    : ");
        String codigoCita = lectorEntrada.nextLine().trim();

        System.out.print("  Código de la receta  : ");
        String codigoReceta = lectorEntrada.nextLine().trim();

        System.out.print("  Medicamentos         : ");
        String listaMedicamentos = lectorEntrada.nextLine().trim();

        System.out.print("  Indicaciones de dosis: ");
        String indicacionesDosis = lectorEntrada.nextLine().trim();

        System.out.print("  Instrucciones de uso : ");
        String instruccionesUso = lectorEntrada.nextLine().trim();

        String mensajeResultado = servicio.emitirReceta(
                codigoCita, codigoReceta, listaMedicamentos, indicacionesDosis, instruccionesUso);

        System.out.println("\n  >> " + mensajeResultado);
        pausar(lectorEntrada);
    }

    /**
     * Muestra la lista completa de citas con todos sus datos.
     *
     * @param servicio Servicio de lógica de negocio activo.
     */
    private static void listarCitas(Service servicio) {
        System.out.println("\n" + LINEA_SIMPLE);
        System.out.println("  LISTA DE CITAS (" + servicio.getCantidadCitas() + " registradas)");
        System.out.println(LINEA_SIMPLE);

        ArrayList<String> listaCitas = servicio.getListaCitasFormateada();
        if (listaCitas.isEmpty()) {
            System.out.println("  No hay citas registradas.");
        } else {
            for (String datosCita : listaCitas) {
                System.out.println(datosCita);
                System.out.println(LINEA_SIMPLE);
            }
        }
    }

    /**
     * Muestra la lista resumida de citas (una línea por cita).
     *
     * @param servicio Servicio de lógica de negocio activo.
     */
    private static void listarCitasResumido(Service servicio) {
        System.out.println("\n" + LINEA_SIMPLE);
        System.out.println("  CITAS — VISTA RESUMIDA");
        System.out.println(LINEA_SIMPLE);

        ArrayList<String> listaCitasResumida = servicio.getListaCitasResumida();
        if (listaCitasResumida.isEmpty()) {
            System.out.println("  No hay citas registradas.");
        } else {
            for (String descripcionCita : listaCitasResumida) {
                System.out.println("  • " + descripcionCita);
            }
        }
        System.out.println();
    }


    // Resumen de la clínica
    /**
     * Muestra el resumen general de la clínica con sus contadores.
     *
     * @param servicio Servicio de lógica de negocio activo.
     */
    private static void mostrarResumenClinica(Service servicio) {
        System.out.println("\n" + LINEA_SEPARADORA);
        System.out.println("  RESUMEN DE LA CLÍNICA");
        System.out.println(LINEA_SEPARADORA);
        System.out.println(servicio.getClinicaActiva().toString());
        System.out.println(LINEA_SEPARADORA + "\n");
    }


    // Utilidades de UI
      /**
     * Imprime el encabezado estilizado del sistema con el nombre de la sección.
     *
     * @param tituloSeccion Nombre de la sección o clínica a mostrar.
     */
    private static void imprimirEncabezado(String tituloSeccion) {
        System.out.println("\n" + LINEA_SEPARADORA);
        System.out.println("  Sistema de Citas Médicas — " + tituloSeccion);
        System.out.println(LINEA_SEPARADORA);
    }

    /**
     * Lee un número entero del usuario, validando que esté dentro del rango indicado.
     * Repite la solicitud si el valor es inválido.
     *
     * @param lectorEntrada Scanner para leer la entrada.
     * @param mensajeSolicitud Texto del prompt que se muestra al usuario.
     * @param valorMinimo   Valor mínimo aceptado (inclusive).
     * @param valorMaximo   Valor máximo aceptado (inclusive).
     * @return Entero válido ingresado por el usuario.
     */
    private static int leerEnteroConValidacion(Scanner lectorEntrada,
                                               String mensajeSolicitud,
                                               int valorMinimo,
                                               int valorMaximo) {
        int valorLeido = -1;
        boolean entradaValida = false;

        while (!entradaValida) {
            System.out.print(mensajeSolicitud);
            try {
                valorLeido = Integer.parseInt(lectorEntrada.nextLine().trim());
                if (valorLeido >= valorMinimo && valorLeido <= valorMaximo) {
                    entradaValida = true;
                } else {
                    System.out.println("  Ingrese un valor entre " + valorMinimo + " y " + valorMaximo + ".");
                }
            } catch (NumberFormatException excepcionFormato) {
                System.out.println("  Entrada inválida. Ingrese un número entero.");
            }
        }
        return valorLeido;
    }

    /**
     * Lee un número decimal (double) del usuario.
     * Repite la solicitud si el valor es inválido o negativo.
     *
     * @param lectorEntrada    Scanner para leer la entrada.
     * @param mensajeSolicitud Texto del prompt que se muestra al usuario.
     * @return Valor double positivo ingresado por el usuario.
     */
    private static double leerDoubleConValidacion(Scanner lectorEntrada, String mensajeSolicitud) {
        double valorLeido = -1;
        boolean entradaValida = false;

        while (!entradaValida) {
            System.out.print(mensajeSolicitud);
            try {
                valorLeido = Double.parseDouble(lectorEntrada.nextLine().trim());
                if (valorLeido >= 0) {
                    entradaValida = true;
                } else {
                    System.out.println("  El valor no puede ser negativo.");
                }
            } catch (NumberFormatException excepcionFormato) {
                System.out.println("  Entrada inválida. Ingrese un número (use punto como decimal).");
            }
        }
        return valorLeido;
    }

    /**
     * Lee una fecha en formato dd/MM/yyyy del usuario.
     * Repite la solicitud si el formato es incorrecto.
     *
     * @param lectorEntrada    Scanner para leer la entrada.
     * @param mensajeSolicitud Texto del prompt que se muestra al usuario.
     * @return Fecha válida como {@link LocalDate}.
     */
    private static LocalDate leerFechaConValidacion(Scanner lectorEntrada, String mensajeSolicitud) {
        LocalDate fechaLeida = null;
        DateTimeFormatter formateadorFecha = DateTimeFormatter.ofPattern(FORMATO_FECHA);

        while (fechaLeida == null) {
            System.out.print(mensajeSolicitud);
            try {
                fechaLeida = LocalDate.parse(lectorEntrada.nextLine().trim(), formateadorFecha);
            } catch (DateTimeParseException excepcionFecha) {
                System.out.println("  Formato inválido. Use dd/MM/yyyy (ejemplo: 15/04/1990).");
            }
        }
        return fechaLeida;
    }

    /**
     * Lee una fecha y hora en formato dd/MM/yyyy HH:mm del usuario.
     * Repite la solicitud si el formato es incorrecto.
     *
     * @param lectorEntrada    Scanner para leer la entrada.
     * @param mensajeSolicitud Texto del prompt que se muestra al usuario.
     * @return Fecha y hora válida como {@link LocalDateTime}.
     */
    private static LocalDateTime leerFechaHoraConValidacion(Scanner lectorEntrada, String mensajeSolicitud) {
        LocalDateTime fechaHoraLeida = null;
        DateTimeFormatter formateadorFechaHora = DateTimeFormatter.ofPattern(FORMATO_FECHA_HORA);

        while (fechaHoraLeida == null) {
            System.out.print(mensajeSolicitud);
            try {
                fechaHoraLeida = LocalDateTime.parse(lectorEntrada.nextLine().trim(), formateadorFechaHora);
            } catch (DateTimeParseException excepcionFecha) {
                System.out.println("  Formato inválido. Use dd/MM/yyyy HH:mm (ejemplo: 20/04/2026 10:30).");
            }
        }
        return fechaHoraLeida;
    }

    /**
     * Pausa la ejecución hasta que el usuario presione Enter.
     * Se usa para que el usuario pueda leer los resultados antes de continuar.
     *
     * @param lectorEntrada Scanner para leer la entrada del usuario.
     */
    private static void pausar(Scanner lectorEntrada) {
        System.out.print("\n  Presione Enter para continuar...");
        lectorEntrada.nextLine();
    }

    // Datos de demostración
    /**
     * Carga datos de demostración en el sistema para facilitar las pruebas.
     * Este método simula registros previos y demuestra la sobrecarga de métodos.
     *
     * @param servicio Servicio sobre el que se cargarán los datos.
     */
    private static void cargarDatosDemostracion(Service servicio) {

        // ── Registrar médicos por datos primitivos (sobrecarga versión 1)
        servicio.registrarMedico(
                "Carlos", "Solís", "1-0234-0567", "8888-1111",
                "carlos.solis@clinica.cr", "MED-001", 950000.00,
                "ESP-001", "Cardiología", "Estudio y tratamiento del corazón");

        servicio.registrarMedico(
                "María", "Vargas", "2-0345-0678", "7777-2222",
                "maria.vargas@clinica.cr", "MED-002", 880000.00,
                "ESP-002", "Pediatría", "Medicina especializada en niños y adolescentes");

        // ── Registrar pacientes por datos primitivos (sobrecarga versión 1)
        servicio.registrarPaciente(
                "Ana", "Jiménez", "3-0456-0789", "6666-3333",
                "ana.jimenez@correo.cr", "PAC-001",
                LocalDate.of(1990, 6, 15), "O+");

        servicio.registrarPaciente(
                "Luis", "Mora", "4-0567-0890", "5555-4444",
                "luis.mora@correo.cr", "PAC-002",
                LocalDate.of(1985, 11, 20), "A-");

        // ── Programar citas
        servicio.programarCita(
                "CIT-001", "Control de presión arterial",
                LocalDateTime.of(2026, 5, 10, 9, 0),
                "MED-001", "PAC-001");

        servicio.programarCita(
                "CIT-002", "Revisión pediátrica general",
                LocalDateTime.of(2026, 5, 12, 11, 30),
                "MED-002", "PAC-002");

        // Confirmar una cita para demostrar cambio de estado
        servicio.confirmarCita("CIT-001");

        System.out.println("  Datos de demostración cargados correctamente.");
    }
}