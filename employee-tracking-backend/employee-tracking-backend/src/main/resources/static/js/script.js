// Base API URL configuration
const API_BASE_URL = "http://localhost:8080/api";

// GLOBAL SCOPE MEIN SET-ROLE FUNCTION (Taaki HTML onclick direct access kar sake)
function setRole(role) {
    console.log("Selected role:", role);
    localStorage.setItem('selectedRole', role);
    
    // Agar role ke hisab se UI toggle karni ho ya login page par bhejna ho:
    if(typeof showLoginModal === 'function') {
        showLoginModal(role);
    }
}

// FIX 1: Pure Backend Connected Add Employee Function
async function addEmployee(event) {
  if (event) event.preventDefault();

  console.log("--> Register Employee Triggered!");

  const newEmp = {
    empId: document.getElementById('newEmpId').value.trim(),
    name: document.getElementById('newEmpName').value.trim(),
    username: document.getElementById('newEmpUser').value.trim(),
    password: document.getElementById('newEmpPass').value.trim(),
    baseSalary: parseFloat(document.getElementById('newEmpBaseSalary').value) || 0
  };

  try {
    const response = await fetch(`${API_BASE_URL}/employees`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + (localStorage.getItem('token') || '')
      },
      body: JSON.stringify(newEmp)
    });

    console.log("Response Status:", response.status);

    if (response.ok) {
      const savedDbEmp = await response.json();
      alert("🟢 Employee Successfully Saved in MySQL Database!");

      // Sync local list with new data
      employees.push({
        id: newEmp.empId,
        name: newEmp.name,
        user: newEmp.username,
        pass: newEmp.password,
        baseSalary: newEmp.baseSalary,
        selfie: null,
        checkInTime: null,
        checkInTimestamp: null,
        checkOutTime: null,
        totalWorkHours: null,
        isCheckedIn: false,
        records: [],
        deals: []
      });

      saveEmployeesToStorage();
      if (event.target) event.target.reset();
      renderAdminDashboard();

    } else {
      alert("⚠️ Backend Error (" + response.status + "): Data MySQL me save nahi hua!");
    }
  } catch (err) {
    console.error("Fetch Error:", err);
    alert("❌ Server Connection Failed! Pehle Backend Spring Boot app ko run karo (Port 8080).");
  }
}

// FIX 2: Backend DB Sync on Admin Dashboard Load
async function renderAdminDashboard() {
  // Try fetching real employees from MySQL Backend first
  try {
    const res = await fetch(`${API_BASE_URL}/employees`, {
      headers: {
        'Authorization': 'Bearer ' + (localStorage.getItem('token') || '')
      }
    });
    if (res.ok) {
      const dbEmployees = await res.json();
      console.log("MySQL fetched employees:", dbEmployees);
    }
  } catch (e) {
    console.log("Backend offline, using fallback storage.");
  }

  // Fallback / local storage sync
  if (typeof getStoredEmployees === 'function') {
      window.employees = getStoredEmployees();
  }

  const select = document.getElementById('dealEmpSelect');
  const filterSelect = document.getElementById('filterEmpSelect');
  if (!select || !filterSelect) return;

  const currentFilterVal = filterSelect.value || "all";

  select.innerHTML = '<option value="">Select Employee *</option>';
  filterSelect.innerHTML = '<option value="all">All Employees Data</option>';

  if (Array.isArray(window.employees)) {
      window.employees.forEach((emp, index) => {
        select.innerHTML += `<option value="${index}">${emp.name} (${emp.id})</option>`;
        filterSelect.innerHTML += `<option value="${emp.id}">${emp.name} (${emp.id})</option>`;
      });
  }

  filterSelect.value = currentFilterVal;

  const attBody = document.getElementById('adminAttendanceTable');
  if (attBody && Array.isArray(window.employees)) {
    attBody.innerHTML = '';
    window.employees.forEach(emp => {
      const selfieImg = emp.selfie 
        ? `<img src="${emp.selfie}" class="w-10 h-10 rounded-full object-cover border-2 border-blue-600">`
        : `<span class="w-10 h-10 rounded-full bg-slate-200 flex items-center justify-center text-[10px] text-slate-400">No Pic</span>`;

      const checkIn = emp.checkInTime || '<span class="text-slate-400">Not Checked In</span>';
      const checkOut = emp.checkOutTime || (emp.checkInTime ? '<span class="text-emerald-600 font-semibold">Active Working</span>' : '<span class="text-slate-400">-</span>');
      const duration = emp.totalWorkHours || (emp.checkInTime ? 'In Progress' : '-');

      let statusBadge = `<span class="bg-slate-100 text-slate-500 text-xs px-2.5 py-1 rounded-full font-bold">Offline</span>`;
      if (emp.checkInTime && !emp.checkOutTime) {
        statusBadge = `<span class="bg-emerald-100 text-emerald-800 text-xs px-2.5 py-1 rounded-full font-bold">🟢 Working</span>`;
      } else if (emp.checkInTime && emp.checkOutTime) {
        statusBadge = `<span class="bg-blue-100 text-blue-800 text-xs px-2.5 py-1 rounded-full font-bold">🔴 Completed</span>`;
      }

      attBody.innerHTML += `
        <tr class="border-b hover:bg-slate-50">
          <td class="p-3">${selfieImg}</td>
          <td class="p-3 font-bold text-slate-800">${emp.name}<br><span class="text-xs text-slate-400 font-mono">${emp.id}</span></td>
          <td class="p-3 font-mono font-semibold text-slate-700">${checkIn}</td>
          <td class="p-3 font-mono font-semibold text-slate-700">${checkOut}</td>
          <td class="p-3 font-mono text-xs font-bold text-blue-600">${duration}</td>
          <td class="p-3">${statusBadge}</td>
        </tr>
      `;
    });
  }
}