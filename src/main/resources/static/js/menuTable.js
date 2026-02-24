async function cargarMenu() {
  const response = await fetch("/productos");
  const productos = await response.json();

  const platosBody = document.getElementById("platos-body");
  const bebidasBody = document.getElementById("bebidas-body");

  platosBody.innerHTML = "";
  bebidasBody.innerHTML = "";

  productos.forEach((p) => {
    const fila = `
            <tr>
                <td>${p.nombre}</td>
                <td>${p.descripcion}</td>
                <td>$${p.precio}</td>
            </tr>
        `;

    if (p.categoria === "PLATO") {
      platosBody.innerHTML += fila;
    } else {
      bebidasBody.innerHTML += fila;
    }
  });
}

cargarMenu();
