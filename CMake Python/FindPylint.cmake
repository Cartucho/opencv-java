execute_process(
    COMMAND pylint --version
    OUTPUT_VARIABLE pylint_out
    RESULT_VARIABLE pylint_error
    ERROR_VARIABLE pylint_suppress)

if (NOT pylint_error)
    string(REGEX MATCH "pylint .\..\.." pylint_version_string "${pylint_out}")
    string(SUBSTRING "${pylint_version_string}" 7 5 pylint_version)
endif ()

if (pylint_version)
    set(PYLINT_FOUND 1
        CACHE INTERNAL "Pylint version ${pylint_version} found")
endif ()

include(FindPackageHandleStandardArgs)
find_package_handle_standard_args(Pylint REQUIRED_VARS pylint_version
                                    VERSION_VAR pylint_version)
