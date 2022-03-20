IF EXISTS(SELECT
            *
          FROM sys.objects
          WHERE object_id = OBJECT_ID(N'news_findall')
		  )
  DROP PROCEDURE news_findall
GO

CREATE PROCEDURE news_findall
          @pageNumber  INT =1,
          @pageSize INT = 10,
		  @searchByTitle nvarchar(250) =null,
		  @merchantId int = null
      AS

       DECLARE @cnt INT
       DECLARE @offset INT

              SELECT COUNT(*) AS cnt FROM [news] (NOLOCK)
			  WHERE
			   	(merchant_id = @merchantId or @merchantId is null) and
				(title like '%' + @searchByTitle + '%' or @searchByTitle is null)

               SET @pageNumber = ABS(@pageNumber)
               SET @pageSize = ABS(@pageSize)
               IF @pageNumber < 1
                SET @pageNumber = 1
               IF @pageSize < 1
                SET @pageSize = 1

               SET @offset = (@pageNumber - 1) * @pageSize

               SELECT * FROM [news] (NOLOCK)
			   WHERE
					(merchant_id = @merchantId or @merchantId is null) and
					(title like '%' + @searchByTitle + '%' or @searchByTitle is null)

               ORDER BY id OFFSET @offset ROWS FETCH NEXT @pageSize ROWS ONLY


GO


IF EXISTS(SELECT
            *
          FROM sys.objects
          WHERE object_id = OBJECT_ID(N'news_insertupdate')
		  )
  DROP PROCEDURE news_insertupdate
GO

CREATE PROCEDURE [dbo].news_insertupdate
	@Id bigint,
	@Title nvarchar(255) = NULL,
	@Summary nvarchar(500),
	@Content nvarchar(max),
	@imageUrl nvarchar(255),
	@pageUrl varchar(255),
	@tags nvarchar(255),
	@type nvarchar(255),
	@Category nvarchar(255) = NULL,
	@MerchantId int = null,

	@modifiedBy nvarchar(255),
	@createdBy nvarchar(255),
	@dateCreated  datetime = getdate,
	@dateModified datetime = getdate
AS

IF EXISTS(SELECT [Id] FROM [dbo].news (NOLOCK) WHERE [Id] = @Id and merchant_id = @MerchantId)
BEGIN
	UPDATE [dbo].news SET
		[title] = @Title,
		[summary] = @Summary,
		[content] = @Content,
		[category] = @Category,
		[image_url] = @imageUrl,
		[page_url] = @pageUrl,
		[tags] = @tags,
		[type] = @type,
		[merchant_id] = @MerchantId,
		--Audit Properties --
		modified_by = @modifiedBy,
		date_modified = @dateModified
	WHERE
		([Id] = @Id ) and merchant_id = @MerchantId

	 SELECT @Id = app.id FROM [dbo].[news] app (nolock)
	 WHERE  ([Id] = @Id ) and [merchant_id] = @MerchantId
RETURN @Id
END
ELSE
BEGIN

INSERT INTO [dbo].[news](
	[title],
	[summary],
	[content],
	[category],
	[image_url],
	[page_url],
	[tags],
	[type],
	[merchant_id],

	[modified_by],
	[created_by],
	[date_created],
	[date_modified]
) VALUES (
	@Title,
	@Summary,
	@Content,
	@Category,
	@imageUrl,
	@pageUrl,
	@tags,
	@type,
	@MerchantId,

	@modifiedBy,
	@createdBy,
	@dateCreated,
	@dateModified
)

SET @Id = SCOPE_IDENTITY()
SELECT @Id

RETURN @Id

END
 --- end insert


GO
IF EXISTS(SELECT
            *
          FROM sys.objects
          WHERE object_id = OBJECT_ID(N'delete_news_byid')
		  )
  DROP PROCEDURE delete_news_byid

GO
CREATE PROCEDURE [dbo].[delete_news_byid]
	@Id smallint,
	@MerchantId int
AS

DELETE [dbo].[news] WHERE [Id] = @Id and merchant_id =  @MerchantId



GO
IF EXISTS(SELECT
            *
          FROM sys.objects
          WHERE object_id = OBJECT_ID(N'news_getbyid')
		  )
  DROP PROCEDURE [news_getbyid]
GO

CREATE PROCEDURE [dbo].[news_getbyid]
	@Id smallint,
	@merchantId int
AS

SELECT top 1* FROM [dbo].[news] (nolock)
WHERE
	[Id] = @Id and (merchant_id = @merchantId or @merchantId is null)


GO
IF EXISTS(SELECT
            *
          FROM sys.objects
          WHERE object_id = OBJECT_ID(N'news_getbytitle'))
  DROP PROCEDURE news_getbytitle
GO

CREATE PROCEDURE [dbo].[news_getbytitle]
	@title nvarchar(250),
	@merchantId int
AS

SELECT top 1 * FROM [dbo].[news] (nolock)
WHERE title = @title and (merchant_id = @merchantId or @merchantId is null)