const openModal = () => {
    const modal = new bootstrap.Modal(document.getElementById('sessionModal'), {
        keyboard: false,
        backdrop: 'static',
    });
    modal.show();
    document.querySelector('.modal-content').style.animation = 'fadeIn 0.4s ease';
};

const closeModal = () => {
    const modal = bootstrap.Modal.getInstance(document.getElementById('sessionModal'));
    modal.hide();
};

// Dynamiser la date de début et de fin
const dateDebut = document.getElementById('dateDebut');
const dateFin = document.getElementById('dateFin');

dateDebut.addEventListener('change', () => {
    dateFin.min = dateDebut.value;
});
