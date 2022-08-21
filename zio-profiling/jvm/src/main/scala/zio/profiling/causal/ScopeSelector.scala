/*
 * Copyright 2021 John A. De Goes and the ZIO Contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package zio.profiling.causal

import zio.profiling.Tag

final case class ScopeSelector(run: Tag => Option[Tag])

object ScopeSelector {
  def below(scope: Tag): ScopeSelector =
    ScopeSelector { tag =>
      if (tag.isValid) scope.getDirectDescendant(tag) else None
    }

  def belowRecursive(scope: Tag): ScopeSelector =
    ScopeSelector { tag =>
      if (tag.isValid && scope.isPrefixOf(tag)) Some(tag) else None
    }

  val Default: ScopeSelector =
    below(Tag.Root)
}