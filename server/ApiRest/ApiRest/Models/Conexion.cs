//------------------------------------------------------------------------------
// <auto-generated>
//     Este código se generó a partir de una plantilla.
//
//     Los cambios manuales en este archivo pueden causar un comportamiento inesperado de la aplicación.
//     Los cambios manuales en este archivo se sobrescribirán si se regenera el código.
// </auto-generated>
//------------------------------------------------------------------------------

namespace ApiRest.Models
{
    using System;
    using System.Collections.Generic;
    
    public partial class Conexion
    {
        [System.Diagnostics.CodeAnalysis.SuppressMessage("Microsoft.Usage", "CA2214:DoNotCallOverridableMethodsInConstructors")]
        public Conexion()
        {
            this.ConexionSesion = new HashSet<ConexionSesion>();
        }
    
        public System.Guid id { get; set; }
        public System.DateTime entrada { get; set; }
        public Nullable<System.DateTime> salida { get; set; }
        public Nullable<int> duracion { get; set; }
        public Nullable<long> usuarioId { get; set; }
    
        public virtual Usuario Usuario { get; set; }
        [System.Diagnostics.CodeAnalysis.SuppressMessage("Microsoft.Usage", "CA2227:CollectionPropertiesShouldBeReadOnly")]
        public virtual ICollection<ConexionSesion> ConexionSesion { get; set; }
    }
}
