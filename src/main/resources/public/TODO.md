rename attributes in .html / remove unneeded ones
rename stuff in main.css ; ids, classes
rename stuff in app.js ; func names, var names
rearrange attributes in .html e.g. id->class->etc.
clean up index.html
remove unused angular directives/services in controller input args e.g. $location
-------------------------------------------

synchronized services / multithreading
- handle records that have been deleted/edited recently -- dealing with stale client side data
    highlight table row if stale ?
    poll ? (have this as an option) e.g. https://blog.guya.net/2016/08/08/simple-server-polling-in-angularjs-done-right/
- handle records that already exist
toggle search -- hasHistory & vice versa
    check box below search box
attach files
add dueForPPMs in search
- add spinner to buttons (?)
    add spinner to load/pagination/refresh
- [bug] column sorting (e.g. dates) not accurate
    change ppmDateInLong/tncDateInLong to YYYYMMDD String
filter due ppms status + serialNumber in due PPM popup
resizable table column width
    - nowrap column / long column text ?
- date validation
- onRefresh -- navbar loses data if not on machines.html
- exceptionHandling in $http.get
synchronization is slow
- distinguish updates to history and machine -- get/setHistoryCount should be
    separate exceptions too
- show machine no. on history page -- which machine's history?
- add icon to buttons/notifications -- e.g. success, failure, add, delete, save
have a separate addHistory option
- encrypt password
export as...
add "createdBy" info/field
admin dashboard
    show logs
    set permissions
registration
    user profiles
users - lastLoggedOn
in-app communication sys
reportedBy as url/links to user profile

recurring db backup
connect github with heroku
upgrade heroku
upgrade mlab
custom domain name
    custom ssl -- https://letsencrypt.org/getting-started/
addons [https://elements.heroku.com/addons]
    email/sms (e.g. due ppm)
    memcache
    app monitor
    log monitor
    maintenance/status pages
db performance
    indexing

re-design idea:: instead of tables, why not a list of "posts"?
re-design idea:: actions go out of the table as buttons (on top of the table?) -- select by click on row / checkbox / radio button

how do i improve perf for due ppms alerts?
pagination:: showing records 1-50 & etc.
    OR option to show how many records per page

-------------------------------------------
why no (err) color during form validation ?
date picker (?)
auto-refresh table data
historyCount, createdOn, & lastUpdatedOn can be a row hover data type (?)
    e.g. <tr ...title="Last updated on: {{machine.lastUpdated}}">
draggable pop-up/modal
table rows -- show details in hover
double-click rows ?
should lastUpdatedOn/dateOfCreation have seconds on timestamp (?)
change table row hover highlight color
show custom <blank> page if no history/machine (?)
