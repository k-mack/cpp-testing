# Gradle-Catch

Illustrates how to use Catch with Gradle. Currently, Gradle does not support
the Catch automated test framework. This repository attempts to show one
possible way to use Gradle and Catch together.

## Prerequisites

The tasks associated with code coverage require `gcov` and `lcov` to be
installed.

## Relevant Tasks

To see the relevant tasks to the objective of this repository, run:

```
gradlew tasks
```

Scroll up to view the tasks under the "Test tasks".

### Using Catch with Gradle

The approach taken to integrate Catch with Gradle is to separate the test cases
from their test driver. This involves creating two extra components in the
Gradle model.  The test cases are encapsulated into a library and are
statically linked into the test driver, which is an executable. The reason for
this separation is to avoid long compile times with Catch. In this way, changes
can be made to the test code without having to rebuild the Catch-generated
`main()`. The test driver, therefore, should never (or rarely) change. I
suppose one could check-in the test driver's build outputs to VCS to avoid
having to build them upon a clean repository clone, but I won't do that here.

A test driver is created for each component that is to be tested. This
component only contains

```cpp
// main.cpp
#define CATCH_CONFIG_MAIN
#include "catch.hpp"
```

This is based off the information provided by Catch about its own problems with
compilation times. (see
https://github.com/philsquared/Catch/blob/master/docs/slow-compiles.md).

#### The Quirk of This Approach

Separating the test code from the test driver is a straight-forward approach to
working around Catch's long compile times. Unfortunately, this requires to
statically link the whole library containing the test code to the test driver.
I say unfortunately because Gradle doesn't currently provide a mechanism for
libraries to communicate to downstream binaries how it should be linked.
Instead, the downstream binaries need to make that decision. This typically
isn't a problem for most projects, but this short-coming does make itself known
in this approach of integrating Catch with Gradle. (And maybe this isn't a
short-coming and I'm just being too opinionated. Either way, Gradle is an
awesome tool.)

The solution (I found) is to customize the `toolChains` of the project's model.
This customization involves inspecting the linker arguments, removing the
default argument by which the test code's library will be linked and replacing
it with arguments that tell the toolChain's linker to link the whole library
(as opposed to just the parts the test driver needs). Statically linking the
whole library is the known approach to getting Catch to work with a library of
test cases (see https://github.com/philsquared/Catch/issues/720). Since this
customization takes place inside the `toolChains` block of the model, it needs
to be bounded to only when Catch is used. To accomplish, the customization is
only applied when either the property `catch` or `code_coverage` is defined.
So, if we want to run the tests, then we need to do so using

```
gradlew test -Pcatch
```

If we don't define one of those properties (`code_coverage` needs to be defined
for the reasons mentioned in the next section), then zero tests will be ran
simply because the test code's static library won't be linked as a whole
archive.

#### Code Coverage

Every loves code coverage, right? Regardless, I thought it would be interesting
to get Catch, GCOV, and LCOV glued together using Gradle. It actually falls
nicely into the work-around I described above for Catch in that you need to
statically link the whole test code library into the test driver to get an
accurate code coverage measurement.

If you have GCOV and LCOV installed, run

```
gradlew openCodeCoverage -Pcode_coverage
```

This will run the code coverage tasks and open your browser to generated HTML
output from LCOV.
