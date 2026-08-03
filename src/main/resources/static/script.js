const API_BASE = "/customer";

const rowsBody      = document.getElementById("customerRows");
const entryCountEl  = document.getElementById("entryCount");
const emptyState    = document.getElementById("emptyState");
const toastEl       = document.getElementById("toast");

const tabs          = document.querySelectorAll("[data-view]");
const pages          = {
    all:    document.getElementById("view-all"),
    lookup: document.getElementById("view-lookup"),
    new:    document.getElementById("view-new"),
};

const customerForm  = document.getElementById("customerForm");
const formTitle     = document.getElementById("formTitle");
const cardTab       = document.getElementById("cardTab");
const submitBtn     = document.getElementById("submitBtn");
const cancelEditBtn = document.getElementById("cancelEdit");
const customerIdField = document.getElementById("customerId");

const lookupForm    = document.getElementById("lookupForm");
const lookupResult  = document.getElementById("lookupResult");

// ---------- View switching ----------

function showView(view) {
    Object.entries(pages).forEach(([key, el]) => el.hidden = key !== view);
    document.querySelectorAll(".tab").forEach(t => {
        t.classList.toggle("active", t.dataset.view === view);
    });
    if (view === "all") loadCustomers();
}

document.querySelectorAll("[data-view]").forEach(el => {
    el.addEventListener("click", () => showView(el.dataset.view));
});

// ---------- Toast ----------

let toastTimer;
function showToast(message, isError = false) {
    clearTimeout(toastTimer);
    toastEl.textContent = message;
    toastEl.classList.toggle("error", isError);
    toastEl.classList.add("show");
    toastTimer = setTimeout(() => toastEl.classList.remove("show"), 3200);
}

// ---------- Rendering ----------

function renderRows(customers) {
    rowsBody.innerHTML = "";
    emptyState.hidden = customers.length > 0;
    entryCountEl.textContent = `${customers.length} ${customers.length === 1 ? "entry" : "entries"}`;

    customers.forEach(c => {
        const tr = document.createElement("tr");
        tr.innerHTML = `
      <td class="cell-no">${String(c.customerId).padStart(4, "0")}</td>
      <td class="cell-name">${escapeHtml(c.name || "")}</td>
      <td class="cell-muted">${escapeHtml(c.email || "—")}</td>
      <td class="cell-muted">${escapeHtml(c.phone || "—")}</td>
      <td class="cell-muted">${escapeHtml(c.address || "")}</td>
      <td>
        <div class="row-actions">
          <button class="icon-btn" data-action="edit" data-id="${c.customerId}">Edit</button>
          <button class="icon-btn danger" data-action="delete" data-id="${c.customerId}">Delete</button>
        </div>
      </td>
    `;
        rowsBody.appendChild(tr);
    });
}

function renderLookupRow(container, c) {
    container.innerHTML = `
    <table class="ledger-table">
      <thead>
        <tr><th class="col-no">No.</th><th>Name</th><th>Email</th><th>Phone</th><th>Address</th></tr>
      </thead>
      <tbody>
        <tr>
          <td class="cell-no">${String(c.customerId).padStart(4, "0")}</td>
          <td class="cell-name">${escapeHtml(c.name || "")}</td>
          <td class="cell-muted">${escapeHtml(c.email || "—")}</td>
          <td class="cell-muted">${escapeHtml(c.phone || "—")}</td>
          <td class="cell-muted">${escapeHtml(c.address || "")}</td>
        </tr>
      </tbody>
    </table>
  `;
}

function escapeHtml(str) {
    const div = document.createElement("div");
    div.textContent = str;
    return div.innerHTML;
}

// ---------- API calls ----------

async function loadCustomers() {
    try {
        const res = await fetch(`${API_BASE}/getAll`);
        if (!res.ok) throw new Error("Could not load the ledger.");
        const data = await res.json();
        renderRows(data);
    } catch (err) {
        showToast(err.message, true);
    }
}

async function getCustomerById(id) {
    const res = await fetch(`${API_BASE}/get?id=${id}`);
    if (!res.ok) throw new Error("No account found under that number.");
    return res.json();
}

async function addCustomer(payload) {
    const res = await fetch(`${API_BASE}/addCustomer`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
    });
    if (!res.ok) throw new Error(await extractError(res, "Could not add the customer."));
    return res;
}

async function updateCustomer(id, payload) {
    const res = await fetch(`${API_BASE}/update?id=${id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
    });
    if (!res.ok) throw new Error(await extractError(res, "Could not update the customer."));
    return res;
}

async function deleteCustomer(id) {
    const res = await fetch(`${API_BASE}/delete?id=${id}`, { method: "DELETE" });
    if (!res.ok) throw new Error("Could not delete that account.");
    return res;
}

async function extractError(res, fallback) {
    try {
        const text = await res.text();
        return text || fallback;
    } catch {
        return fallback;
    }
}

// ---------- Form handling (add / edit) ----------

function resetForm() {
    customerForm.reset();
    customerIdField.value = "";
    formTitle.textContent = "Open a New Account";
    cardTab.textContent = "New Entry";
    submitBtn.textContent = "Enter into Ledger";
    cancelEditBtn.hidden = true;
}

function fillFormForEdit(c) {
    customerIdField.value = c.customerId;
    document.getElementById("name").value = c.name || "";
    document.getElementById("email").value = c.email || "";
    document.getElementById("phone").value = c.phone || "";
    document.getElementById("address").value = c.address || "";
    formTitle.textContent = `Amend Account No. ${String(c.customerId).padStart(4, "0")}`;
    cardTab.textContent = "Editing Entry";
    submitBtn.textContent = "Save Changes";
    cancelEditBtn.hidden = false;
}

customerForm.addEventListener("submit", async (e) => {
    e.preventDefault();
    const payload = {
        name: document.getElementById("name").value.trim(),
        email: document.getElementById("email").value.trim(),
        phone: document.getElementById("phone").value.trim(),
        address: document.getElementById("address").value.trim(),
    };
    const id = customerIdField.value;

    try {
        if (id) {
            await updateCustomer(id, payload);
            showToast("Account updated in the ledger.");
        } else {
            await addCustomer(payload);
            showToast("New account entered into the ledger.");
        }
        resetForm();
        showView("all");
    } catch (err) {
        showToast(err.message, true);
    }
});

cancelEditBtn.addEventListener("click", () => {
    resetForm();
    showView("all");
});

// ---------- Row actions (edit / delete) ----------

rowsBody.addEventListener("click", async (e) => {
    const btn = e.target.closest("button[data-action]");
    if (!btn) return;
    const id = btn.dataset.id;

    if (btn.dataset.action === "edit") {
        try {
            const customer = await getCustomerById(id);
            fillFormForEdit(customer);
            showView("new");
        } catch (err) {
            showToast(err.message, true);
        }
    }

    if (btn.dataset.action === "delete") {
        if (!confirm(`Remove account No. ${String(id).padStart(4, "0")} from the ledger?`)) return;
        try {
            await deleteCustomer(id);
            showToast("Account removed from the ledger.");
            loadCustomers();
        } catch (err) {
            showToast(err.message, true);
        }
    }
});

// ---------- Lookup ----------

lookupForm.addEventListener("submit", async (e) => {
    e.preventDefault();
    const id = document.getElementById("lookupId").value;
    try {
        const customer = await getCustomerById(id);
        renderLookupRow(lookupResult, customer);
    } catch (err) {
        lookupResult.innerHTML = `<p class="not-found">${err.message}</p>`;
    }
});

// ---------- Init ----------

loadCustomers();