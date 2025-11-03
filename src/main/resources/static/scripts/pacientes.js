    document.addEventListener("DOMContentLoaded", () => {
                // Alertas desaparecen automáticamente
                const alertBox = document.getElementById("alertContainer");
                if (alertBox) setTimeout(() => { alertBox.style.opacity="0"; setTimeout(()=>alertBox.remove(),500); },3500);

                // Variables formulario y tabla
                const rows = document.querySelectorAll("#pacientesTableBody tr");
                const form = {
                    id: document.getElementById("paciente-id"),
                    nombre: document.getElementById("nombre"),
                    apellido: document.getElementById("apellido"),
                    dni: document.getElementById("dni"),
                    diagnostico: document.getElementById("diagnostico"),
                    observaciones: document.getElementById("observaciones")
                };
                const formTitle = document.getElementById("formTitle");
                const submitBtn = document.getElementById("submitBtn");
                const deleteBtn = document.getElementById("btnEliminar");
                const cancelarBtn = document.getElementById("btnCancelar");
                let selected = null;

                // Modo edición
                function setEditMode(row){
                    form.id.value = row.dataset.id || "";
                    const cells = row.cells;
                    const fullName = cells[1].textContent.trim().split(' ');
                    form.nombre.value = fullName[0] || '';
                    form.apellido.value = fullName.slice(1).join(' ') || '';
                    form.dni.value = cells[2].textContent.trim();
                    form.diagnostico.value = cells[3].textContent.trim();
                    form.observaciones.value = cells[4].textContent.trim();

                    if(selected) selected.classList.remove("bg-primary/20","border-2","border-yellow-400");
                    row.classList.add("bg-primary/20","border-2","border-yellow-400");
                    selected = row;

                    formTitle.textContent = "Editar Paciente";
                    submitBtn.textContent = "Actualizar";
                    deleteBtn.classList.remove("hidden");
                    cancelarBtn.classList.remove("hidden");
                }

                // Modo agregar
                function setAddMode(){
                    form.id.value = "";
                    form.nombre.value = "";
                    form.apellido.value = "";
                    form.dni.value = "";
                    form.diagnostico.value = "";
                    form.observaciones.value = "";

                    if(selected) selected.classList.remove("bg-primary/20","border-2","border-yellow-400");
                    selected = null;

                    formTitle.textContent = "Agregar Paciente";
                    submitBtn.textContent = "Agregar";
                    deleteBtn.classList.add("hidden");
                    cancelarBtn.classList.add("hidden");
                }

                // Eventos
                rows.forEach(r=>r.addEventListener("click",()=>setEditMode(r)));
                cancelarBtn.addEventListener("click", setAddMode);

                // Búsqueda en tiempo real
                document.getElementById("searchInput").addEventListener("input", e => {
                    const text = e.target.value.toLowerCase();
                    rows.forEach(r => r.style.display = r.textContent.toLowerCase().includes(text) ? "" : "none");
                });

                // Modal eliminación
                const confirmBtn = document.getElementById("confirmDelete");
                const cancelBtn = document.getElementById("cancelDelete");
                const modal = document.getElementById("confirmModal");
                const deleteIdInput = document.getElementById("deleteId");
                const deleteForm = document.getElementById("deleteForm");

                deleteBtn.addEventListener("click", ()=>{
                    if(!form.id.value) return alert("Seleccione un paciente para eliminar");
                    deleteIdInput.value = form.id.value;
                    modal.classList.remove("hidden");
                    modal.classList.add("flex");
                });

                confirmBtn.addEventListener("click", ()=>{
                    modal.classList.add("hidden");
                    deleteForm.submit(); // POST a Spring backend
                });

                cancelBtn.addEventListener("click", ()=> modal.classList.add("hidden"));

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