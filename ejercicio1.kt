//  1. ENUM: los roles posibles 
enum class Rol {
    ADMIN, EDITOR, VISITANTE
}

// 2. DATA CLASS: los datos de un usuario 
data class Usuario(
    val nombre: String,
    val correo: String,
    val clave: String,
    val rol: Rol
)

//  3. INTERFACE: el contrato de autenticación 
interface Autenticable {
    fun autenticar(correo: String, clave: String): Usuario?
}

//  4. OBJECT: el gestor único de usuarios 
object GestorUsuarios : Autenticable {

    private val usuarios = mutableListOf<Usuario>()

    fun registrar(usuario: Usuario) {
        for (u in usuarios) {
            if (u.correo == usuario.correo) {
                println("Error: ya existe un usuario con el correo ${usuario.correo}")
                return
            }
        }
        usuarios.add(usuario)
        println("Usuario ${usuario.nombre} registrado como ${usuario.rol}")
    }

    fun listarUsuarios() {
        println("--- Lista de usuarios ---")
        for (u in usuarios) {
            println("${u.nombre} - ${u.correo} - ${u.rol}")
        }
    }

    override fun autenticar(correo: String, clave: String): Usuario? {
        for (u in usuarios) {
            if (u.correo == correo) {
                if (u.clave == clave) {
                    return u
                }
            }
        }
        return null
    }

    fun validarPermisos(usuario: Usuario) {
        when (usuario.rol) {
            Rol.ADMIN -> println("${usuario.nombre} puede crear, editar y eliminar usuarios")
            Rol.EDITOR -> println("${usuario.nombre} puede editar contenido, pero no eliminar usuarios")
            Rol.VISITANTE -> println("${usuario.nombre} solo puede ver el contenido")
        }
    }
}

//  5. MAIN: aquí arranca el programa 
fun main() {
    // Registrar usuarios
    GestorUsuarios.registrar(Usuario("Samuel", "samuel@correo.com", "1234", Rol.ADMIN))
    GestorUsuarios.registrar(Usuario("Laura", "laura@correo.com", "abcd", Rol.EDITOR))
    GestorUsuarios.registrar(Usuario("Pedro", "pedro@correo.com", "0000", Rol.VISITANTE))

    // Intentar registrar un correo repetido
    GestorUsuarios.registrar(Usuario("Otro", "samuel@correo.com", "9999", Rol.VISITANTE))

    // Mostrar la lista
    GestorUsuarios.listarUsuarios()

    // Login correcto
    println("--- Login ---")
    val usuario = GestorUsuarios.autenticar("laura@correo.com", "abcd")
    if (usuario != null) {
        println("Bienvenida, ${usuario.nombre}")
        GestorUsuarios.validarPermisos(usuario)
    } else {
        println("Correo o clave incorrectos")
    }

    // Login con clave mala
    val intento = GestorUsuarios.autenticar("pedro@correo.com", "clavemala")
    if (intento != null) {
        println("Bienvenido, ${intento.nombre}")
    } else {
        println("Correo o clave incorrectos")
    }

    val kotlin = "🙂"
    println(kotlin)
}
