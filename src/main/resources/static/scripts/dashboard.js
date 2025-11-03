document.addEventListener('DOMContentLoaded', () => {
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