const API_URL = "/sampledb/api/manufacturers";
const AUTH_HEADER = "Basic " + btoa("manager:123");
let isEditMode = false;
document.addEventListener("DOMContentLoaded", () => {
    loadTableData();
    document.getElementById("manufacturer-form").addEventListener("submit", handleSave);
    document.getElementById("btn-clear").addEventListener("click", clearForm);
    document.getElementById("btn-delete").addEventListener("click", handleDelete);
});
// FETCH GET: Carrega todos os fabricantes e popula a tabela
// FETCH GET: Carrega todos os fabricantes e popula a tabela de forma 100% segura
function loadTableData() {
    fetch(API_URL, {
        method: "GET",
        headers: {"Authorization": AUTH_HEADER, "Accept": "application/json"}
    })
            .then(res => {
                if (res.status === 401 || res.status === 403)
                    throw new Error("Acesso negado: Verifique suas credenciais.");
                if (!res.ok)
                    throw new Error("Erro no servidor ao ler registros de fabricantes.");
                return res.json();
            })
            .then(data => {
                const tbody = document.getElementById("manufacturer-table-body");
                tbody.innerHTML = "";
                if (data.length === 0) {
                    tbody.innerHTML = ` < tr > <td colspan="4" class="p-4 text-center text-gray-400 italic">Nenhum fab r icante cadastrado.</td> < /tr>`;
                    return;
                }

                data.forEach(item => {
                    const row = document.createElement("tr");
                    row.className = "hover:bg-gray-50 transition";
                    const city = item.city ? item.city.trim() : "";
                    const state = item.state ? item.state.trim() : "";
                    const location = (city && state) ? `${city}/${state}` : (city || state || "Não informado");
                    const email = item.email ? item.email.trim() : "N/A";
                    // 1. Criamos a tag do link isolada na memória do JavaScript
                    const link = document.createElement("a");
                    link.href = "#";
                    link.className = "text-blue-600 hover:text-blue-800 font-bold hover:underline";
                    link.innerText = item.manufacturerId;
                    // 2. Amarrarmos o evento de clique DIRETAMENTE no objeto, passando a referência viva do 'item'
                    link.addEventListener("click", (event) => {
                        event.preventDefault();
                        selectRecord(item); // Sem aspas, sem stringify, 100% Type-Safe!
                    });
                    // 3. Montamos a estrutura das colunas de texto da linha
                    row.innerHTML = `
                        <td class="p-3"></td> <!-- A primeira coluna nascerá vazia para receber o link -->
                        <td class="p-3 font-medium text-gray-700">${item.name}</td>
                        <td class="p-3 text-gray-600">${location}</td>
                        <td class="p-3 text-gray-500">${email}</td>
                    `;
                    // 4. Injetamos o link seguro criado na memória direto na primeira célula (td) da linha
                    row.querySelector("td").appendChild(link);
                    tbody.appendChild(row);
                });
            })
            .catch(err => showMessage("error-box", err.message));
}

// Ação: Ao clicar no link do ID, preenche o formulário inteiro para edição
function selectRecord(item) {
    // Atualiza o input oculto e o label visual
    document.getElementById("manufacturerId").value = item.manufacturerId;
    document.getElementById("manufacturerIdLabel").innerText = item.manufacturerId;
    document.getElementById("manufacturerIdLabel").classList.remove("text-gray-500");
    document.getElementById("manufacturerIdLabel").classList.add("text-gray-800");
    document.getElementById("name").value = item.name || "";
    document.getElementById("addressLine1").value = item.addressLine1 || "";
    document.getElementById("addressLine2").value = item.addressLine2 || "";
    document.getElementById("city").value = item.city || "";
    document.getElementById("state").value = item.state || "";
    document.getElementById("phone").value = item.phone || "";
    document.getElementById("fax").value = item.fax || "";
    document.getElementById("email").value = item.email || "";
    document.getElementById("rep").value = item.rep || "";
    document.getElementById("form-title").innerText = "Update Manufacturer: " + item.name;
    document.getElementById("btn-delete").classList.remove("hidden");
    isEditMode = true;
    hideMessages();
}

// FETCH POST: Envia o payload completo do Record DTO para salvar ou atualizar
function handleSave(event) {
    event.preventDefault();
    hideMessages();
    const idValue = document.getElementById("manufacturerId").value;
    const payload = {
        // Se estiver vazio (Novo), envia null para o TomEE gerar automaticamente. Se for edição, envia o ID numérico.
        manufacturerId: idValue ? parseInt(idValue) : null,
        name: document.getElementById("name").value.trim(),
        addressLine1: document.getElementById("addressLine1").value.trim() || null,
        addressLine2: document.getElementById("addressLine2").value.trim() || null,
        city: document.getElementById("city").value.trim() || null,
        state: document.getElementById("state").value.toUpperCase().trim() || null,
        phone: document.getElementById("phone").value.trim() || null,
        fax: document.getElementById("fax").value.trim() || null,
        email: document.getElementById("email").value.trim() || null,
        rep: document.getElementById("rep").value.trim() || null
    };
    fetch(API_URL, {
        method: "POST",
        headers: {
            "Authorization": AUTH_HEADER,
            "Content-Type": "application/json"
        },
        body: JSON.stringify(payload)
    })
            .then(res => {
                if (!res.ok)
                    return res.text().then(textErr => {
                        throw new Error(textErr || "Erro ao processar os dados cadastrais no servidor.");
                    });
                showMessage("success-box", isEditMode ? "Fabricante atualizado com sucesso!" : "Novo fabricante cadastrado com sucesso!");
                clearForm();
                loadTableData();
            })
            .catch(err => showMessage("error-box", err.message));
}

// FETCH DELETE: Remove o fabricante pelo ID
function handleDelete() {
    const id = document.getElementById("manufacturerId").value;
    const name = document.getElementById("name").value;
    if (!id || !confirm(`Tem certeza que deseja excluir permanentemente o fabricante "${name}"?`))
        return;

    hideMessages();

    fetch(`${API_URL}/${id}`, {
        method: "DELETE",
        headers: {"Authorization": AUTH_HEADER}
    })
            .then(res => {
                if (!res.ok)
                    throw new Error("Não foi possível remover. O fabricante pode estar vinculado a produtos ativos.");
                showMessage("success-box", "Fabricante removido com sucesso do catálogo do sistema.");
                clearForm();
                loadTableData();
            })
            .catch(err => showMessage("error-box", err.message));
}

// Utilitários de Reset e Mensagens
function clearForm() {
    document.getElementById("manufacturer-form").reset();

    // Reseta o controle do ID
    document.getElementById("manufacturerId").value = "";
    document.getElementById("manufacturerIdLabel").innerText = "Auto-generated";
    document.getElementById("manufacturerIdLabel").classList.remove("text-gray-800");
    document.getElementById("manufacturerIdLabel").classList.add("text-gray-500");

    document.getElementById("form-title").innerText = "New Manufacturer";
    document.getElementById("btn-delete").classList.add("hidden");
    isEditMode = false;
}

function showMessage(elementId, text) {
    const box = document.getElementById(elementId);
    box.innerText = text;
    box.classList.remove("hidden");
    setTimeout(() => box.classList.add("hidden"), 5000);
}

function hideMessages() {
    document.getElementById("error-box").classList.add("hidden");
    document.getElementById("success-box").classList.add("hidden");
}
