const API_URL = "/sampledb/api/customers";
const DISCOUNT_API_URL = "/sampledb/api/discount-codes";
const ZIP_API_URL = "/sampledb/api/micro-markets";

// CAPTURA INTELIGENTE: Pega o cabeçalho salvo na sessão. 
// Caso não exista nada lá ainda, usa o manager de contingência para não travar seu teste local.
const AUTH_HEADER = sessionStorage.getItem("active_auth") || ("Basic " + btoa("manager:123"));

let isEditMode = false;

document.addEventListener("DOMContentLoaded", () => {
    // Inicializa as duas caixas de seleção relacionais em paralelo
    loadDiscountCombo();
    loadZipCombo();
    loadTableData();

    document.getElementById("customer-form").addEventListener("submit", handleSave);
    document.getElementById("btn-clear").addEventListener("click", clearForm);
    document.getElementById("btn-delete").addEventListener("click", handleDelete);
});

// COMBO FILLER 1: Alimenta os planos de desconto corporativos
function loadDiscountCombo() {
    fetch(DISCOUNT_API_URL, {
        method: "GET",
        headers: {"Authorization": AUTH_HEADER, "Accept": "application/json"}
    })
            .then(res => res.json())
            .then(data => {
                const select = document.getElementById("discountCode");
                select.innerHTML = '<option value="">Select a Discount Tier</option>';
                data.forEach(item => {
                    const opt = document.createElement("option");
                    opt.value = item.discountCode;
                    opt.innerText = item.discountCode + " (" + parseFloat(item.rate).toFixed(2) + "%)";
                    select.appendChild(opt);
                });
            })
            .catch(() => {
                document.getElementById("discountCode").innerHTML = '<option value="">Failed to sync discounts</option>';
            });
}

// COMBO FILLER 2: Alimenta as rotas de logística de CEPs
function loadZipCombo() {
    fetch(ZIP_API_URL, {
        method: "GET",
        headers: {"Authorization": AUTH_HEADER, "Accept": "application/json"}
    })
            .then(res => res.json())
            .then(data => {
                const select = document.getElementById("zipCode");
                select.innerHTML = '<option value="">Select Logistics Territory</option>';
                data.forEach(item => {
                    const opt = document.createElement("option");
                    opt.value = item.zipCode;
                    opt.innerText = "Zip: " + item.zipCode + " (Radius: " + item.radius + ")";
                    select.appendChild(opt);
                });
            })
            .catch(() => {
                document.getElementById("zipCode").innerHTML = '<option value="">Failed to sync territories</option>';
            });
}
// FETCH GET: Carrega todos os clientes e monta a tabela de contas ativa
function loadTableData() {
    fetch(API_URL, {
        method: "GET",
        headers: {"Authorization": AUTH_HEADER, "Accept": "application/json"}
    })
            .then(res => {
                if (res.status === 401 || res.status === 403)
                    throw new Error("Access Denied: Privilégios insuficientes para ler carteira.");
                if (!res.ok)
                    throw new Error("Erro no servidor ao ler balanço de clientes.");
                return res.json();
            })
            .then(data => {
                const tbody = document.getElementById("customer-table-body");
                tbody.innerHTML = "";

                if (data.length === 0) {
                    tbody.innerHTML = '<tr><td colspan="6" class="p-4 text-center text-gray-400 italic">Nenhuma conta de cliente registrada.</td></tr>';
                    return;
                }

                data.forEach(item => {
                    const row = document.createElement("tr");
                    row.className = "hover:bg-gray-50 transition";

                    // Navegação segura nas propriedades das Entidades Relacionais associadas
                    const discountId = item.discountCode ? item.discountCode.discountCode : "N/A";
                    const zipId = item.microMarket ? item.microMarket.zipCode : "N/A";

                    const city = item.city ? item.city.trim() : "";
                    const state = item.state ? item.state.trim() : "";
                    const location = (city && state) ? (city + "/" + state) : (city || state || "Não informado");
                    const credit = item.creditLimit ? parseFloat(item.creditLimit).toFixed(2) : "0.00";

                    // Criação do link seguro em memória pura do Javascript
                    const link = document.createElement("a");
                    link.href = "#";
                    link.className = "text-blue-600 hover:text-blue-800 font-bold hover:underline";
                    link.innerText = item.customerId;
                    link.addEventListener("click", (event) => {
                        event.preventDefault();
                        selectRecord(item, discountId, zipId);
                    });

                    // Concatenação de string tradicional à prova de falhas do parser do NetBeans
                    row.innerHTML = '<td></td>' +
                            '<td class="p-3 font-semibold text-gray-700">' + item.name + '</td>' +
                            '<td class="p-3 text-gray-600">' + location + '</td>' +
                            '<td class="p-3 text-gray-500 font-medium">' + zipId + '</td>' +
                            '<td class="p-3 text-center text-gray-600 font-bold">' + discountId + '</td>' +
                            '<td class="p-3 text-right font-semibold text-green-700">$' + credit + '</td>';

                    // Acopla o link na primeira coluna vazia da linha
                    row.querySelector("td").className = "p-3";
                    row.querySelector("td").appendChild(link);

                    tbody.appendChild(row);
                });
            })
            .catch(err => showMessage("error-box", err.message));
}

// Ação: Ao selecionar a linha do link, reidrata o formulário inteiro e bloqueia a PK
function selectRecord(item, discountId, zipId) {
    document.getElementById("customerId").value = item.customerId;
    document.getElementById("customerId").disabled = true; // Chave primária imutável no update

    document.getElementById("name").value = item.name || "";
    document.getElementById("discountCode").value = discountId;
    document.getElementById("zipCode").value = zipId;
    document.getElementById("creditLimit").value = item.creditLimit || 0;
    document.getElementById("addressLine1").value = item.addressLine1 || "";
    document.getElementById("addressLine2").value = item.addressLine2 || "";
    document.getElementById("city").value = item.city || "";
    document.getElementById("state").value = item.state || "";
    document.getElementById("phone").value = item.phone || "";
    document.getElementById("fax").value = item.fax || "";
    document.getElementById("email").value = item.email || "";

    document.getElementById("form-title").innerText = "Update Account: " + item.name;
    document.getElementById("btn-delete").classList.remove("hidden");
    isEditMode = true;
    hideMessages();
}

// FETCH POST: Dispara o payload estruturado (Record DTO) mapeando as chaves para salvar/atualizar
function handleSave(event) {
    event.preventDefault();
    hideMessages();

    const payload = {
        customerId: parseInt(document.getElementById("customerId").value),
        name: document.getElementById("name").value.trim(),
        discountCode: document.getElementById("discountCode").value,
        zipCode: document.getElementById("zipCode").value,
        creditLimit: parseInt(document.getElementById("creditLimit").value),
        addressLine1: document.getElementById("addressLine1").value.trim(),
        addressLine2: document.getElementById("addressLine2").value.trim() || null,
        city: document.getElementById("city").value.trim(),
        state: document.getElementById("state").value.toUpperCase().trim(),
        phone: document.getElementById("phone").value.trim(),
        fax: document.getElementById("fax").value.trim() || null,
        email: document.getElementById("email").value.trim()
    };

    // IMPRESSÃO DE FLAGRANTE: Abre o F12, limpa o console e clique em Salvar para ver estas duas linhas:
    console.log("==> ENVIANDO TOKEN AUTH:", AUTH_HEADER);
    console.log("==> PAYLOAD ESTRUTURADO:", JSON.stringify(payload));

    fetch(API_URL, {
        method: "POST",
        headers: {"Authorization": AUTH_HEADER, "Content-Type": "application/json"},
        body: JSON.stringify(payload)
    })
            .then(res => {
                if (!res.ok) {
                    // Se der erro, obriga o navegador a ler o Stack Trace ou mensagem detalhada do TomEE
                    return res.text().then(textErr => {
                        throw new Error(textErr || "Erro interno 500 no servidor Apache TomEE.");
                    });
                }
                showMessage("success-box", "Conta corporativa salva e integrada com sucesso.");
                clearForm();
                loadTableData();
            })
            .catch(err => {
                // Remove o teto da mensagem genérica e joga o erro real na tela!
                showMessage("error-box", err.message);
            });
}

// FETCH DELETE: Remove fisicamente a conta de cliente (Operação Admin-Only no Resource)
function handleDelete() {
    const id = document.getElementById("customerId").value;
    const name = document.getElementById("name").value;
    if (!id || !confirm("Deseja deletar permanentemente a conta de '" + name + "'?"))
        return;

    hideMessages();

    fetch(API_URL + "/" + id, {
        method: "DELETE",
        headers: {"Authorization": AUTH_HEADER}
    })
            .then(res => {
                if (res.status === 403)
                    throw new Error("Acesso Proibido: Operadores não possuem permissão para excluir contas.");
                if (!res.ok)
                    throw new Error("Remoção rejeitada. O cliente possui ordens de compra ativas no sistema.");
                showMessage("success-box", "Conta do cliente removida da carteira corporativa.");
                clearForm();
                loadTableData();
            })
            .catch(err => showMessage("error-box", err.message));
}

// Utilitários de Interface e Limpeza
function clearForm() {
    document.getElementById("customer-form").reset();
    document.getElementById("customerId").disabled = false;
    document.getElementById("form-title").innerText = "New Customer Account";
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
