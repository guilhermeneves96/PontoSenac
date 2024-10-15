// status.js

document.addEventListener("DOMContentLoaded", function() {
    const statusTd = document.getElementById('statusTd');
    const status = statusTd.textContent.trim();

    switch (status) {
        case 'PENDENTE':
            statusTd.classList.add('pendentes');
            break;
        case 'CANCELADO':
            statusTd.classList.add('cancelado');
            break;
        case 'RECUSADO':
            statusTd.classList.add('recusado');
            break;
        case 'CONCLUÍDO':
            statusTd.classList.add('concluido');
            break;
        default:
            break;
    }
});
