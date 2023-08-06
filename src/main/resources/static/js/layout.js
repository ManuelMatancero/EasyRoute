const body = document.querySelector("body"),
  modeToggle = body.querySelector(".mode-toggle");
sidebar = body.querySelector("nav");
sidebarToggle = body.querySelector(".sidebar-toggle");
tableElement = body.querySelector("table");
tableContainer = body.querySelector(".table-container");
tableContainer2 = body.querySelector(".table-container2");

let getMode = localStorage.getItem("mode");
if (getMode && getMode === "dark") {
  body.classList.toggle("dark");
  tableElement.classList.toggle("table-dark");
  tableContainer.classList.toggle("bg-dark");
  tableContainer2.classList.toggle("bg-dark");
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
