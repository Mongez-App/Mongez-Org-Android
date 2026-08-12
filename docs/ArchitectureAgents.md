# Architecture Agents
> **Platform:** Android (Jetpack Compose)
> **Architecture:** MVI + Clean Architecture
> **Domain:** Organization Management Platform

---

## 1. System Agents (MVI Actors)

The presentation layer operates on a unidirectional data flow governed by specific MVI agents. These agents ensure a predictable state machine for complex screens like the Admin Dashboard and Team Management.

*   **State Manager (The Single Source of Truth):** Holds the mutually exclusive `ViewState` and persists critical data across state changes.
*   **Intent Processor (User Actions & System Triggers):** Acts as the ingestion engine for all UI interactions. It maps intents (e.g., `CreateTeam`, `AssignTask`, `InviteMember`) to specific Domain Use Cases.
*   **Effect Dispatcher (One-Off Side Effects):** Handles transient UI events that should not be persisted in the state, such as navigation routing, displaying Snackbars, or triggering one-time Toasts.

## 2. Domain & Data Agents (App Flow Coordinators)

To prevent repository bloat, data coordination is handled per *App Flow* rather than per feature.

*   **App Flow Repositories:** Coordinators that manage data operations for a complete user journey (e.g., `AuthenticationFlowRepository`, `TeamManagementFlowRepository`, `CourseManagementFlowRepository`, `MemberManagementFlowRepository`). They orchestrate multiple data sources to serve the domain layer.
*   **Use Case Interactors:** Granular, single-responsibility agents residing in the Domain layer. They execute specific business rules, such as validating member invitations, scheduling AI-generated tasks, or managing team course assignments.

## 3. AI & Business Logic Agents

The Organization Management Platform relies on specialized intelligent agents to handle dynamic task scheduling and team management.

### Magic Box — AI Task Scheduler
This algorithmic agent dynamically generates and schedules tasks for team members based on their preferences, course content, and organizational requirements.

| Trigger | Action | Result |
| :--- | :--- | :--- |
| Admin starts a course for a team | Generates tasks based on course content and member preferences | Personalized task schedules for each team member |
| New event added (quiz, exam, deadline) | Increases priority weight of the corresponding course | Reactive rescheduling of member task timelines |
| Member joins a new team | Merges organizational and private course tasks | Unified study schedule across all enrolled teams |

### Task Priority Engine
This agent operates within the scheduling system to balance workload across organizational and private courses.

| Capability | Constraint | Output |
| :--- | :--- | :--- |
| Workload Balancing | Based on member preferences and deadlines | Optimized task distribution across courses |
| Event-Aware Scheduling | Considers team events (quizzes, exams, projects) | Priority-adjusted task timelines |

---

## 4. Development Guidelines

### Dependency Management
When adding a new dependency, it must be added to the version catalog file (`gradle/libs.versions.toml`) first, and then referenced in the appropriate `build.gradle.kts` file.

### Coding Conventions
- **No Fully Qualified Names (FQNs):** When using a class or function, do not use its fully qualified name inline. Always import it and use its simple name.
  *   **Do:** `private fun navigateToTeams(team: Team)` or `delay(1000L)`
  *   **Don't:** `private fun navigateToTeams(team: com.iti.mongez.org.domain.team.model.Team)` or `kotlinx.coroutines.delay(1000L)`
- **Modern UI Components:** Always use the most up-to-date and modern Jetpack Compose APIs. Avoid using deprecated or obsolete composables.
  *   **Do:** `HorizontalDivider(...)`
  *   **Don't:** `Divider(...)` (Deprecated)
- **Component Reusability:** When creating generic UI elements (like `MemberCard`), place them directly in the `design_system` module rather than duplicating them locally inside presentation screens.
- **No Hardcoded Values:** Do not use hardcoded dimensions (dp/sp), colors, or alpha values in the UI. Always use `Theme.spacing.*`, `Theme.colorScheme.*`, `Theme.typography.*`, and `Theme.radius.*` from the design system to ensure consistency.
- **Compose Previews:** Every screen must include a `@Preview` function to visualize its layout and state easily during development. Provide dummy data that reflects a realistic state.