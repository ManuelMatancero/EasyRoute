document.addEventListener("DOMContentLoaded", function () {
    const filterInput = document.getElementById("filterInput");
    const filterTypeRadios = document.querySelectorAll('input[name="filterType"]');
    const table = document.getElementById("dataTable");

    filterInput.addEventListener("keyup", filterTable);
    filterTypeRadios.forEach(radio => radio.addEventListener("change", filterTable));

    function filterTable() {
        const filterText = filterInput.value.trim().toLowerCase();
        const filterType = document.querySelector('input[name="filterType"]:checked').value;
        const rows = table.getElementsByTagName("tr");

        for (let i = 1; i < rows.length; i++) {
            const row = rows[i];
            const cellValue = row.getElementsByTagName("td")[getFilterIndex(filterType)].innerText.toLowerCase();
            const showRow = filterText === "" || cellValue.includes(filterText);

            row.style.display = showRow ? "" : "none";
        }
    }

    function getFilterIndex(filterType) {
        switch (filterType) {
            case "name":
                return 2;
            case "code":
                return 1;
            case "day":
                return 9;
            case "route":
                return 8;
            case "nameCustomer":
                return 1;
            case "idCustomer":
                return 3;
            case "routeCustomer":
                return 4;
            case "cobradorCustomer":
                return 5;
            case "numeroPagare":
                return 1;
            case "nombreCliente":
                return 2;
            case "vencimiento" :
                return 6;
            case "rutaNombre" :
                return 1;
            case "rutaDia" :
                return 2;
            case "rutaZona" :
                return 3;
            case "rutaCobrador" :
                return 4;
            default:
                return 0; // Default to name filter if no match found
        }

    }
});
