document.addEventListener('DOMContentLoaded', () => {
        // Referencias a contenedores y elementos del DOM
        const infoEnfermera = document.getElementById('info-enfermera');
        const infoCama = document.getElementById('info-cama');
        const camasContainer = document.getElementById('camas-container');
        const enfermerasContainer = document.getElementById('enfermeras-container');
        const categoriasContainer = document.getElementById('categorias-container');

        const modal = document.getElementById('modal-eliminar');
        const modalContent = modal.querySelector('.modal-content');
        const btnConfirmar = document.getElementById('btn-confirmar');
        const btnCancelar = document.getElementById('btn-cancelar');
        let asignacionAEliminar = null;

        // --- MODAL ELIMINAR ---
        function showModalEliminar(id) {
            asignacionAEliminar = id;
            modal.classList.add('modal-show');
            setTimeout(() => modalContent.classList.add('modal-content-show'), 10);
        }

        // Cancelar eliminación
        btnCancelar.addEventListener('click', () => {
            modalContent.classList.remove('modal-content-show');
            setTimeout(() => modal.classList.remove('modal-show'), 200);
            asignacionAEliminar = null;
        });

        // Confirmar eliminación vía fetch AJAX
        btnConfirmar.addEventListener('click', () => {
            if (!asignacionAEliminar) return;
            fetch(`/hospitalito/asignaciones/api/eliminar?id=${asignacionAEliminar}`)
                .then(res => res.json())
                .then(data => {
                    if(data.success){
                        showFlashcard(data.message || 'Asignación eliminada correctamente', 'success');
                        document.querySelectorAll(`[data-id="${asignacionAEliminar}"]`).forEach(btn => btn.closest('div').remove());
                        actualizarInfoEnfermera(document.getElementById('hidden-enfermera').value);
                        actualizarInfoCama(document.getElementById('hidden-cama').value);
                    } else {
                        showFlashcard(data.message || 'No se pudo eliminar', 'error');
                    }
                })
                .catch(err => showFlashcard('Error al eliminar asignación', 'error'));
            modalContent.classList.remove('modal-content-show');
            setTimeout(() => modal.classList.remove('modal-show'), 200);
            asignacionAEliminar = null;
        });

        // --- FLASHCARDS: mostrar mensajes temporales ---
        function showFlashcard(message, type='success', duration=3500){
            const container = document.createElement('div');
            container.className = `flex items-center gap-3 p-4 rounded-lg shadow-md animate-fadeIn
                ${type==='success'?'bg-green-100 text-green-800':'bg-red-100 text-red-800'}`;
            container.innerHTML = `<span class="material-symbols-outlined">${type==='success'?'check_circle':'error'}</span>
                <p class="font-medium">${message}</p>`;

            const toastContainer = document.getElementById('toast-container');
            toastContainer.appendChild(container);

            container.offsetHeight; // Forzar reflow para animación

            // Desaparecer después del tiempo definido
            setTimeout(() => {
                container.style.transition = 'opacity 0.5s ease';
                container.style.opacity = '0';
                container.addEventListener('transitionend', () => {
                    if(container.parentNode) container.parentNode.removeChild(container);
                });
            }, duration);
        }

        // --- BUSCADORES CON AUTOCOMPLETADO ---
        function setupSearch(inputId, listId, hiddenId){
            const input=document.getElementById(inputId);
            const list=document.getElementById(listId);
            const hidden=document.getElementById(hiddenId);

            function showAllItems(){
                Array.from(list.children).forEach(li=>li.style.display='block');
                list.style.display='block';
            }

            input.addEventListener('input', ()=>{
                const filter=input.value.toLowerCase();
                let anyVisible=false;
                Array.from(list.children).forEach(li=>{
                    const text=li.textContent.toLowerCase();
                    li.style.display=text.includes(filter)?'block':'none';
                    if(text.includes(filter)) anyVisible=true;
                });
                list.style.display=anyVisible?'block':'none';
            });

            input.addEventListener('click', showAllItems);

            Array.from(list.children).forEach(li=>{
                li.addEventListener('click', ()=>{
                    input.value=li.textContent;
                    hidden.value=li.dataset.value;
                    hidden.dispatchEvent(new Event('change'));
                    list.style.display='none';
                });
            });

            document.addEventListener('click', e=>{
                if(!input.contains(e.target)&&!list.contains(e.target)) list.style.display='none';
            });
        }

        setupSearch('buscar-enfermera','lista-enfermeras','hidden-enfermera');
        setupSearch('buscar-cama','lista-camas','hidden-cama');

        const selectEnfermera=document.getElementById('hidden-enfermera');
        const selectCama=document.getElementById('hidden-cama');

        // --- ACTUALIZAR INFO DINÁMICA DE ENFERMERA ---
        function actualizarInfoEnfermera(id){
            if(!id){ infoEnfermera.classList.add('hidden'); camasContainer.innerHTML=''; return; }
            fetch(`/hospitalito/asignaciones/api/enfermeras/${id}/asignaciones`)
                .then(res=>res.json())
                .then(data=>{
                    infoEnfermera.classList.remove('hidden');
                    camasContainer.innerHTML='';
                    data.camas.forEach(c=>{
                        const div=document.createElement('div');
                        div.className="p-4 rounded-lg shadow-md bg-card-light dark:bg-card-dark border border-gray-200 dark:border-gray-700";
                        div.innerHTML=`<h4 class="font-semibold">Cama ${c.id}</h4>
                            <p>Categoría: ${c.categoria}</p>
                            <p>Ubicación: ${c.ubicacion}</p>
                            <button class="btn-eliminar mt-3 px-4 py-2 bg-red-500 hover:bg-red-600 text-white rounded-lg transition-colors" data-id="${c.idAsignacion}">Eliminar Asignación</button>`;
                        camasContainer.appendChild(div);
                    });
                });
        }

        // --- ACTUALIZAR INFO DINÁMICA DE CAMA ---
        function actualizarInfoCama(id){
            if(!id){ infoCama.classList.add('hidden'); enfermerasContainer.innerHTML=''; return; }
            fetch(`/hospitalito/asignaciones/api/camas/${id}/asignaciones`)
                .then(res=>res.json())
                .then(data=>{
                    infoCama.classList.remove('hidden');
                    enfermerasContainer.innerHTML='';
                    data.enfermeras.forEach(e=>{
                        const div=document.createElement('div');
                        div.className="p-4 rounded-lg shadow-md bg-card-light dark:bg-card-dark border border-gray-200 dark:border-gray-700";
                        div.innerHTML=`<h4 class="font-semibold">Enfermera ${e.id}</h4>
                            <p>Nombre: ${e.nombre}</p>
                            <p>Apellido: ${e.apellido}</p>
                            <p>Dni: ${e.dni}</p>
                            <button class="btn-eliminar mt-3 px-4 py-2 bg-red-500 hover:bg-red-600 text-white rounded-lg transition-colors" data-id="${e.idAsignacion}">Eliminar Asignación</button>`;
                        enfermerasContainer.appendChild(div);
                    });
                });
        }

        selectEnfermera.addEventListener('change', ()=>actualizarInfoEnfermera(selectEnfermera.value));
        selectCama.addEventListener('change', ()=>actualizarInfoCama(selectCama.value));

        // --- DELEGACIÓN DE EVENTOS PARA BOTONES ELIMINAR ---
        document.addEventListener('click', e=>{
            if(e.target.matches('.btn-eliminar')){
                showModalEliminar(e.target.dataset.id);
            }
        });

        // --- SUBMIT FORMULARIO ASIGNAR CAMA CON AJAX ---
        const formAsignar = document.getElementById('form-asignar');
        formAsignar.addEventListener('submit', e => {
            if (!selectEnfermera.value || !selectCama.value) return; // Validación nativa si falta campo
            e.preventDefault();
            const formData = new FormData(formAsignar);

            fetch('/hospitalito/asignaciones', {
                method: 'POST',
                body: formData
            })
            .then(res => res.json())
            .then(data => {
                if (data.success) {
                    showFlashcard(data.message, 'success');
                    actualizarInfoEnfermera(selectEnfermera.value);
                    actualizarInfoCama(selectCama.value);
                } else {
                    showFlashcard(data.message, 'error');
                }
            })
            .catch(() => showFlashcard('Error al asignar la cama', 'error'));
        });

        // --- CARGAR RESUMEN DE CATEGORÍAS ---
        fetch('/hospitalito/asignaciones/api/categorias/resumen')
            .then(res=>res.json())
            .then(data=>{
                categoriasContainer.innerHTML='';
                data.forEach(c=>{
                    const div=document.createElement('div');
                    div.className="p-4 rounded-lg shadow-md bg-card-light dark:bg-card-dark border border-gray-200 dark:border-gray-700";
                    div.innerHTML=`<h4 class="font-semibold">${c.categoria}</h4><p>Máx Enfermeras: ${c.maxEnfermeras}</p>`;
                    categoriasContainer.appendChild(div);
                });
            });

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
                      localStorage.setItem('theme', 'dark')
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