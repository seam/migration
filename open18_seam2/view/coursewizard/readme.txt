Currently the stateful (pageflow) model is enabled. To switch to the ad hoc model:

#1 enable ad hoc navigation rules in /WEB-INF/pages.xml
#2 swap the command buttons on each page of the wizard with the ones inside the <ui:remove> tags.
#3 remove the pageflow attribute from the @Begin annotations on CourseWizard
