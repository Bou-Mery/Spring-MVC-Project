document.addEventListener("DOMContentLoaded", function() {
    const monthNames = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
    const weekdays = ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"];
    let currentYear = new Date().getFullYear();
    let currentMonth = new Date().getMonth();

    const monthNameElement = document.getElementById("month-name");
    const calendarWeekdaysElement = document.getElementById("calendar_weekdays");
    const calendarContentElement = document.getElementById("calendar_content");

    function renderCalendar() {
        monthNameElement.innerText = `${monthNames[currentMonth]} ${currentYear}`;
        calendarWeekdaysElement.innerHTML = weekdays.map(day => `<div>${day}</div>`).join('');

        const firstDayOfMonth = new Date(currentYear, currentMonth, 1).getDay();
        const daysInMonth = new Date(currentYear, currentMonth + 1, 0).getDate();
        calendarContentElement.innerHTML = '';

        // Add blank days for the first week
        for (let i = 0; i < firstDayOfMonth; i++) {
            calendarContentElement.innerHTML += '<div class="blank"></div>';
        }

        // Add days of the month
        for (let day = 1; day <= daysInMonth; day++) {
            const isToday = (day === new Date().getDate() && currentMonth === new Date().getMonth() && currentYear === new Date().getFullYear());
            calendarContentElement.innerHTML += `<div class="${isToday ? 'today' : ''}">${day}</div>`;
        }
    }

    document.getElementById("prev-month").addEventListener("click", function() {
        currentMonth--;
        if (currentMonth < 0) {
            currentMonth = 11;
            currentYear--;
        }
        renderCalendar();
    });

    document.getElementById("next-month").addEventListener("click", function() {
        currentMonth++;
        if (currentMonth > 11) {
            currentMonth = 0;
            currentYear++;
        }
        renderCalendar();
    });

    renderCalendar();
});