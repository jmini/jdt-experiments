package fr.jmini.lerimpoc.api;

public class ProblemReportConfiguration {

    private boolean reportWarnings = false;
    private boolean reportResourceProcessingErrorsAsWarnings = true;

    public boolean isReportWarnings() {
        return reportWarnings;
    }

    public void setReportWarnings(boolean reportWarnings) {
        this.reportWarnings = reportWarnings;
    }

    public boolean isReportResourceProcessingErrorsAsWarnings() {
        return reportResourceProcessingErrorsAsWarnings;
    }

    public void setReportResourceProcessingErrorsAsWarnings(boolean reportResourceProcessingErrorsAsWarnings) {
        this.reportResourceProcessingErrorsAsWarnings = reportResourceProcessingErrorsAsWarnings;
    }

    @Override
    public String toString() {
        return "ProblemReportConfiguration [reportWarnings=" + reportWarnings + ", reportResourceProcessingErrorsAsWarnings=" + reportResourceProcessingErrorsAsWarnings + "]";
    }

}
