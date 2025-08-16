// package codeoptimizer

// import codeoptimizer.Utils.{elementFirstType, reportOptimization}
// import dotty.tools.dotc.*
// import dotty.tools.dotc.ast.tpd.*
// import dotty.tools.dotc.core.*
// import dotty.tools.dotc.core.Contexts.*
// import dotty.tools.dotc.core.Names.*
// import dotty.tools.dotc.core.Symbols.*
// import dotty.tools.dotc.core.Types.*

// class SeqFilterForallOptimizer(using Context) extends AbstractOptimizer:
//   override def transformApply(tree: Apply)(using Context): Apply =
//     tree match
//       case Apply(
//             Select(
//               Apply(
//                 Select(seqExpr, call1),
//                 List(call1Param)
//               ),
//               call2
//             ),
//             List(call2Param)
//           )
//           if call1.mangledString == "filter"
//             && call2.mangledString == "forall"
//             && seqExpr.tpe <:< defn.SeqClass.typeRef.appliedTo(TypeBounds.empty) =>

//         reportOptimization(getClass, s"filter→forall", tree)

//         (for elementType <- elementFirstType(seqExpr) yield
//           val opsSym    = requiredModule(s"codeoptimizer.SeqOps")
//           val methodSym = opsSym.info.decl(termName("filterForall"))

//           Apply(
//             TypeApply(
//               Select(ref(opsSym), methodSym.name),
//               List(TypeTree(elementType))
//             ),
//             List(call1Param, call2Param, seqExpr)
//           ).withSpan(tree.span)
//         ).getOrElse(tree)
//       case _ =>
//         tree
