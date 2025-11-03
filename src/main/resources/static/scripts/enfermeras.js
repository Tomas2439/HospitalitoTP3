document.addEventListener("DOMContentLoaded", () => {
// ==================== ALERTAS ====================
const alertBox = document.getElementById("alertContainer");
if (alertBox) {
    setTimeout(() => {
        alertBox.style.opacity = "0";
         setTimeout(() => alertBox.remove(), 500);
    }, 3500);
}

                // ==================== VARIABLES FORMULARIO ====================
                const rows = document.querySelectorAll("#enfermerasTableBody tr");
                const form = {
                    id: document.getElementById("nurse-id-hidden"),
                    name: document.getElementById("nurse-name"),
                    lastname: document.getElementById("nurse-lastname"),
                    dni: document.getElementById("nurse-dni"),
                    phone: document.getElementById("nurse-phone"),
                    active: document.getElementById("nurse-active")
                };
                const formTitle = document.getElementById("formTitle");
                const submitBtn = document.getElementById("submitBtn");
                const deleteBtn = document.getElementById("btnEliminarNurse");
                const cancelarBtn = document.getElementById("btnCancelarEdicion");
                let selected = null;

                // ==================== MODAL INACTIVO ====================
                const formEnfermera = document.getElementById("formEnfermera");
                const modalInactivo = document.getElementById("modalInactivo");
                const btnCancelarInactivo = document.getElementById("cancelarInactivo");
                const btnConfirmarInactivo = document.getElementById("confirmarInactivo");
                let estadoOriginal = null;

                // ==================== MODO EDICIÓN ====================
                function setEditMode(row) {
                    form.id.value = row.dataset.id || "";
                    const cells = row.cells;
                    const fullName = cells[1].textContent.trim().split(' ');
                    form.name.value = fullName[0] || '';
                    form.lastname.value = fullName.slice(1).join(' ') || '';
                    form.dni.value = cells[2].textContent.trim();
                    form.phone.value = cells[3].textContent.trim();
                    form.active.value = cells[4].textContent.includes("Activo") ? "true" : "false";

                    estadoOriginal = form.active.value;

                    if (selected) selected.classList.remove("bg-primary/20", "border-2", "border-yellow-400");
                    row.classList.add("bg-primary/20", "border-2", "border-yellow-400");
                    selected = row;

                    formTitle.textContent = "Editar Enfermera";
                    submitBtn.textContent = "Actualizar";
                    deleteBtn.classList.remove("hidden");
                    cancelarBtn.classList.remove("hidden");
                }

                // ==================== MODO AGREGAR ====================
                function setAddMode() {
                    form.id.value = "";
                    form.name.value = "";
                    form.lastname.value = "";
                    form.dni.value = "";
                    form.phone.value = "";
                    form.active.value = "true";

                    if (selected) selected.classList.remove("bg-primary/20", "border-2", "border-yellow-400");
                    selected = null;

                    formTitle.textContent = "Agregar Enfermera";
                    submitBtn.textContent = "Agregar";
                    deleteBtn.classList.add("hidden");
                    cancelarBtn.classList.add("hidden");
                }

                // ==================== EVENTOS ====================
                rows.forEach(r => r.addEventListener("click", () => setEditMode(r)));
                cancelarBtn.addEventListener("click", () => setAddMode());

                document.getElementById("searchInput").addEventListener("input", (e) => {
                    const text = e.target.value.toLowerCase();
                    rows.forEach(r => r.style.display = r.textContent.toLowerCase().includes(text) ? "" : "none");
                });

                // ==================== MODAL ELIMINACIÓN ====================
                deleteBtn.addEventListener("click", () => {
                    if (!form.id.value) return alert("Seleccione una enfermera para eliminar");
                    const modal = document.getElementById("confirmModal");
                    const confirmBtn = document.getElementById("confirmDelete");
                    const cancelBtn = document.getElementById("cancelDelete");

                    modal.classList.remove("hidden"); modal.classList.add("flex");

                    confirmBtn.onclick = () => { modal.classList.add("hidden"); window.location.href = `/hospitalito/enfermeras/eliminar?id=${form.id.value}`; };
                    cancelBtn.onclick = () => { modal.classList.add("hidden"); };
                });

                // ==================== SUBMIT INTERCEPT ====================
                formEnfermera.addEventListener("submit", function(e) {
                    const idEnfermera = form.id.value;
                    const nuevoEstado = form.active.value;
                    if (idEnfermera && estadoOriginal === "true" && nuevoEstado === "false") {
                        e.preventDefault();
                        modalInactivo.classList.remove("hidden"); modalInactivo.classList.add("flex");
                    }
                });

                // ==================== BOTONES MODAL INACTIVO ====================
                btnCancelarInactivo.addEventListener("click", function() {
                    modalInactivo.classList.add("hidden"); modalInactivo.classList.remove("flex");
                    form.active.value = estadoOriginal;
                });
                btnConfirmarInactivo.addEventListener("click", function() {
                    modalInactivo.classList.add("hidden"); modalInactivo.classList.remove("flex");
                    formEnfermera.submit();
                });

                // ==================== CIERRE MODAL CON CLICK FUERA O ESC ====================
                modalInactivo.addEventListener("click", e => { if (e.target === modalInactivo) btnCancelarInactivo.click(); });
                document.addEventListener("keydown", e => { if (e.key === "Escape" && !modalInactivo.classList.contains("hidden")) btnCancelarInactivo.click(); });

                // ==================== INICIALIZAR MODO AGREGAR ====================
                setAddMode();

                    // Función para inicializar el modo oscuro
                    function initDarkMode() {
                        const darkModeToggle = document.getElementById('darkModeToggle');
                        const htmlElement = document.documentElement;

                        // Verificar preferencia guardada o preferencia del sistema
                        const savedTheme = localStorage.getItem('theme');
                        const prefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches;

                        if (savedTheme === 'dark' || (!savedTheme && prefersDark)) {
                            htmlElement.classList.add('dark');
                        } else {
                            htmlElement.classList.remove('dark');
                        }

                        // Event listener para el toggle
                        darkModeToggle.addEventListener('click', function() {
                            if (htmlElement.classList.contains('dark')) {
                                htmlElement.classList.remove('dark');
                                localStorage.setItem('theme', 'light');
                            } else {
                                htmlElement.classList.add('dark');
                                localStorage.setItem('theme', 'dark');
                            }
                        });
                    }

                    // Inicializar cuando el DOM esté listo
                    if (document.readyState === 'loading') {
                        document.addEventListener('DOMContentLoaded', initDarkMode);
                    } else {
                        initDarkMode();
                    }
            });