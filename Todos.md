BUG:
//LangaugeSwitcher triggers last toast message
//Change from '' user can have multiple roles' to 'user can have one role'
  - useAppNavigation - navigateByRoles
  - AuthGuard - navigateByRoles
  - NotFound - navigateByRoles
  - Hero - navigateByRoles

// get rid of text-gray-900 dark:text-white etc... so that light and dark mode are the same and working
// download Roboto Font and implement it the right way (now is with url from google)

ON Radar
// changed the index.css and got rid of the primereact recommended way (neccesary?) But is working right now... testing is needed
// did it so that icons work (e.g.pi pi-home) possible future fix... use new icon library
      @layer tailwind-base, primereact, tailwind-utilities;

      /* PrimeReact Theme */
      @import "primereact/resources/themes/lara-light-blue/theme.css";
      @import "primereact/resources/primereact.min.css";
      @import "primeicons/primeicons.css";

      @layer tailwind-base {
        @tailwind base;
      }

      @layer tailwind-utilities {
        @tailwind components;
        @tailwind utilities;
      } 

GENERAL
// translations improvement
// Settings Page
  - Reset Password
  - Theme Toggle
  - Language Selector

COMPONENTS
// Breadcrumbs
// Title bar
// Drawer (Footer)
// Navbar 
    - Avatar Icon with Dropdown
      * Logout
      * Edit
      * Settings
    - Search Bar ?
// Footer

ADMIN
// Data Table for Users
// User Create, Update, Delete, Edit
// Drawer with correct links
// Charts?

CUSTOMER
// Profile Page
// Profile Update
// Drawer with correct links

PAYMENT WITH STRIPE
// Build Feature for Payment with Stripe
// Tier Subscription Component
// Implement Stripe Checkout
// Implement Stripe Webhook
// Implement Stripe Customer Portal
// Implement Stripe Billing Portal
// Implement Stripe Subscription Management
// Implement Stripe Invoices
// Implement Stripe Payment Methods
// Implement Stripe Payment Intents

