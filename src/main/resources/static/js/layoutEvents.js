document.addEventListener("DOMContentLoaded", () => {
  const body = document.querySelector("body");
  const modeToggle = body.querySelector(".mode-toggle");
  const sidebar = body.querySelector("nav");
  const sidebarToggle = body.querySelector(".sidebar-toggle");
  const tableElement = body.querySelector("table");
  const tableContainer = body.querySelector(".table-container");
  const tableContainer2 = body.querySelector(".table-container2");
  const accordionContainer = body.querySelector(".accordion-container");
  const accordionContainer2 = body.querySelector(".accordion-container2");

  let getMode = localStorage.getItem("mode");
  if (getMode && getMode === "dark") {
    tableElement?.classList.toggle("table-dark");
    tableContainer?.classList.toggle("bg-dark");
    tableContainer2?.classList.toggle("bg-dark");
    accordionContainer?.classList.toggle("bg-dark");
    accordionContainer2?.classList.toggle("bg-dark");
  }

  let getStatus = localStorage.getItem("status");
  if (getStatus && getStatus === "close") {
    sidebar.classList.toggle("close");
  }

  modeToggle.addEventListener("click", () => {
    body.classList.toggle("dark");
    tableElement.classList.toggle("table-dark");
    tableContainer.classList.toggle("bg-dark");
    tableContainer2.classList.toggle("bg-dark");
    accordionContainer?.classList.toggle("bg-dark");
    accordionContainer2?.classList.toggle("bg-dark");
    if (body.classList.contains("dark")) {
      localStorage.setItem("mode", "dark");
    } else {
      localStorage.setItem("mode", "light");
    }
  });

  sidebarToggle.addEventListener("click", () => {
    sidebar.classList.toggle("close");
    if (sidebar.classList.contains("close")) {
      localStorage.setItem("status", "close");
    } else {
      localStorage.setItem("status", "open");
    }
  });
});
