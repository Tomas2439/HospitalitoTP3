document.addEventListener("DOMContentLoaded", () => {
    // ===========================
    // Referencias a campos y botones
    // ===========================
    const form = {
        id: document.getElementById("bed-id-hidden"),
        cat: document.getElementById("bed-category"),
        loc: document.getElementById("bed-location"),
        status: document.getElementById("bed-status")
    };
    const formTitle = document.getElementById("formTitle");
    const submitText = document.getElementById("submitText");
    const deleteBtn = document.getElementById("btnEliminarCama");
    const cancelarBtn = document.getElementById("btnCancelarEdicion");
    const camasBody = document.getElementById("camasTableBody");
    const formCama = document.getElementById("formCama");

    let selectedRow = null;
    let currentRow = null;
    let selectedPaciente = null;

    // ===========================
    // ALERTAS FLASHCARDS
    // ===========================
    const alertBox = document.getElementById("alertContainer");
    if (alertBox) {
        setTimeout(() => {
            alertBox.style.opacity = "0";
            setTimeout(() => alertBox.remove(), 500);
        }, 3500);
    }

    // ===========================
    // Limpieza de errores visuales
    // ===========================
    const limpiarErrores = () => {
        document.querySelectorAll("#formCama p").forEach(p => p.textContent = "");
        document.querySelectorAll("#formCama input, #formCama select").forEach(i => i.classList.remove("border-red-600"));
    };

    const hayErroresDeBackend = () => document.querySelectorAll("#formCama p.text-red-600:not(:empty)").length > 0;

    // ===========================
    // Modo edición
    // ===========================
    const setEditMode = (row, mantenerErrores = false) => {
        if (!mantenerErrores) limpiarErrores();

        if (!hayErroresDeBackend() || !mantenerErrores) {
            form.id.value = row.dataset.idCama || "";
            form.cat.value = row.dataset.idCategoria || "";
            form.loc.value = row.cells[2].textContent.trim() || "";
            form.status.value = row.dataset.disponible === "true" ? "true" : "false";
        }

        if (selectedRow) selectedRow.classList.remove("bg-primary/20", "border-2", "border-yellow-400");
        row.classList.add("bg-primary/20", "border-2", "border-yellow-400");
        selectedRow = row;

        formTitle.textContent = "Editar Cama";
        submitText.textContent = "Actualizar";
        deleteBtn.classList.remove("hidden");
        cancelarBtn.classList.remove("hidden");
    };

    // ===========================
    // Modo agregar
    // ===========================
    const setAddMode = (limpiarCampos = true) => {
        if (limpiarCampos) {
            limpiarErrores();
            form.id.value = "";
            form.cat.value = "";
            form.loc.value = "";
            form.status.value = "true";
        }

        if (selectedRow) selectedRow.classList.remove("bg-primary/20", "border-2", "border-yellow-400");
        selectedRow = null;

        formTitle.textContent = "Agregar Cama";
        submitText.textContent = "Agregar";
        deleteBtn.classList.add("hidden");
        cancelarBtn.classList.add("hidden");
    };

    // ===========================
    // Estado inicial
    // ===========================
    const modoEdicion = formCama.dataset.modoEdicion === "true";
    const idEdicion = formCama.dataset.idCama;

    if (hayErroresDeBackend() && modoEdicion && idEdicion) {
        const row = document.querySelector(`#camasTableBody tr[data-id-cama='${idEdicion}']`);
        if (row) setEditMode(row, true);
    } else if (hayErroresDeBackend()) {
        setAddMode(false);
    } else {
        setAddMode(true);
    }

    // Cancelar edición
    cancelarBtn.addEventListener("click", () => setAddMode(true));

    // ===========================
    // Selección de fila en tabla
    // ===========================
    camasBody.addEventListener("click", e => {
        if (e.target.closest("button")) return; // Ignorar clicks en botones
        const row = e.target.closest("tr");
        if (!row) return;
        setEditMode(row);
    });

    // ===========================
    // Filtrado de tabla
    // ===========================
    const search = document.getElementById("searchInput");
    const filter = document.getElementById("filterCategoria");
    const filterTable = () => {
        const text = search.value.toLowerCase();
        const cat = filter.value;
        document.querySelectorAll("#camasTableBody tr").forEach(r => {
            const match =
                (r.cells[2].textContent.toLowerCase().includes(text) ||
                 r.cells[3].textContent.toLowerCase().includes(text)) &&
                (cat === "" || cat === r.dataset.idCategoria);
            r.style.display = match ? "" : "none";
        });
    };
    search.addEventListener("input", filterTable);
    filter.addEventListener("change", filterTable);

    // ===========================
    // Modal Alojar paciente
    // ===========================
    const modal = document.getElementById("modalAlojar");
    const pacienteList = document.getElementById("pacienteList");
    const searchPaciente = document.getElementById("searchPacienteInput");

    camasBody.addEventListener("click", e => {
        if (!e.target.classList.contains("btnAlojar")) return;
        const row = e.target.closest("tr");
        currentRow = row;

        // Abrir modal solo si está disponible
        if (row.dataset.disponible === "true") {
            modal.classList.remove("hidden");
        } else {
            // Desalojar usando POST normal para flashcards
            const form = document.createElement("form");
            form.method = "post";
            form.action = "/hospitalito/camas/desalojar";

            const inputCama = document.createElement("input");
            inputCama.type = "hidden";
            inputCama.name = "idCama";
            inputCama.value = row.dataset.idCama;
            form.appendChild(inputCama);

            document.body.appendChild(form);
            form.submit();
        }
    });

    pacienteList.addEventListener("click", e => {
        const item = e.target.closest("div[data-id-hc]");
        if (!item) return;
        pacienteList.querySelectorAll("div").forEach(d => d.classList.remove("bg-primary/30"));
        item.classList.add("bg-primary/30");
        selectedPaciente = item.dataset.idHc;
    });

    searchPaciente.addEventListener("input", () => {
        const text = searchPaciente.value.toLowerCase();
        pacienteList.querySelectorAll("div[data-id-hc]").forEach(div => {
            div.style.display = div.textContent.toLowerCase().includes(text) ? "" : "none";
        });
    });

    document.getElementById("btnCancelarAlojar").addEventListener("click", () => {
        modal.classList.add("hidden");
        selectedPaciente = null;
    });

    document.getElementById("btnConfirmAlojar").addEventListener("click", () => {
        if (!selectedPaciente) return alert("Seleccione un paciente para alojar");

        // Alojar usando POST normal para flashcards
        const form = document.createElement("form");
        form.method = "post";
        form.action = "/hospitalito/camas/alojar";

        const inputCama = document.createElement("input");
        inputCama.type = "hidden";
        inputCama.name = "idCama";
        inputCama.value = currentRow.dataset.idCama;
        form.appendChild(inputCama);

        const inputHC = document.createElement("input");
        inputHC.type = "hidden";
        inputHC.name = "idHC";
        inputHC.value = selectedPaciente;
        form.appendChild(inputHC);

        document.body.appendChild(form);
        form.submit();
    });

    // ===========================
    // Modal de eliminación
    // ===========================
    deleteBtn.addEventListener("click", () => {
        if (!form.id.value) return alert("Seleccione una cama para eliminar");
        const modal = document.getElementById("confirmModal");
        const confirmBtn = document.getElementById("confirmDelete");
        const cancelBtn = document.getElementById("cancelDelete");

        modal.classList.remove("hidden");
        modal.classList.add("flex");

        confirmBtn.onclick = () => {
            modal.classList.add("hidden");
            window.location.href = `/hospitalito/camas/eliminar?id=${form.id.value}`;
        };

        cancelBtn.onclick = () => {
            modal.classList.add("hidden");
        };
    });

    // ===========================
    // Modo oscuro
    // ===========================
    function initDarkMode() {
        const darkModeToggle = document.getElementById('darkModeToggle');
        const htmlElement = document.documentElement;

        const savedTheme = localStorage.getItem('theme');
        const prefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches;

        if (savedTheme === 'dark' || (!savedTheme && prefersDark)) {
            htmlElement.classList.add('dark');
        } else {
            htmlElement.classList.remove('dark');
        }

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

    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', initDarkMode);
    } else {
        initDarkMode();
    }
});





